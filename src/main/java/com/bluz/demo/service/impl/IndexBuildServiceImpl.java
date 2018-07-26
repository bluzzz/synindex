package com.bluz.demo.service.impl;

import com.bluz.demo.config.IndexConfig;
import com.bluz.demo.config.IndexConfigBean;
import com.bluz.demo.config.IndexConfigFactory;
import com.bluz.demo.domain.IndexBuildResultManager;
import com.bluz.demo.domain.IndexInfo;
import com.bluz.demo.es.IndexPreBuildProcessorFactory;
import com.bluz.demo.exception.IndexBuildException;
import com.bluz.demo.query.AbstractResultSetHandler;
import com.bluz.demo.query.ResultSetHandler;
import com.bluz.demo.query.ResultSetHandlerFactory;
import com.bluz.demo.queue.IndexBuildQueueManager;
import com.bluz.demo.service.IndexBuildService;
import com.bluz.demo.thread.IndexBuildThread;
import com.bluz.demo.thread.IndexBuildThreadPool;
import com.bluz.demo.util.ConnManager;
import com.bluz.demo.es.EsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class IndexBuildServiceImpl implements IndexBuildService {
    @Autowired
    private IndexConfigFactory indexConfigFactory;
    @Autowired
    private IndexBuildQueueManager indexBuildQueueManager;
    @Autowired
    private EsManager esManager;
    @Autowired
    private ResultSetHandlerFactory resultSetHandlerFactory;
    @Autowired
    private ConnManager connManager;
    @Autowired
    private IndexBuildResultManager indexBuildResultManager;
    @Autowired
    private IndexPreBuildProcessorFactory indexPreBuildProcessorFactory;
    @Override
    public void build(String index) throws Exception{

            //检查
            check(index);

            //获取一个队列
            ConcurrentLinkedQueue<String> queue = indexBuildQueueManager.getQueue();
            IndexInfo cache = indexBuildQueueManager.getCache(index);
            IndexInfo indexInfo = IndexInfo
                                    .Builder
                                    .newBuilder(cache)
                                    .setQueue(queue)
                                    .build();
            //保留缓存信息、设置监听
            indexBuildQueueManager.putIndexCache(index,indexInfo);

            indexBuildResultManager.observe(indexInfo);

            //获取对应配置及查询结果处理器
            IndexConfigBean indexConfig = indexConfigFactory.getIndexConfig(index);
            ResultSetHandler queryHandler = resultSetHandlerFactory.getQueryHandler(index,indexBuildQueueManager);

            //创建消费线程
            IndexBuildThread indexBuildThread = new IndexBuildThread(
                    indexBuildResultManager,
                    indexBuildQueueManager,
                    queue,
                    indexConfig,
                    esManager);

            //提交任务
            IndexBuildThreadPool.getInstance().submit(indexBuildThread);

            //查询需要建立索引的数据

            connManager.executeQuery(indexConfig.getQuerySql(),queryHandler);
    }

    /**
     * 检查 索引 是否有效、是否正在进行
     * @param index
     * @throws Exception
     */
    public void check(String index) throws Exception{
        if(index==null || "".equals(index)){
            throw new IndexBuildException("索引无效");
        }

        IndexInfo indexInfo = indexBuildQueueManager.getCache(index);

        if(indexInfo==null){
            indexBuildQueueManager.putIndexCache(index,
                    IndexInfo.Builder
                             .newBuilder()
                             .setIndex(index)
                             .build());
        }
        //是否索引完成
        if(indexInfo!=null && !indexInfo.getDataIndexedState()){
            throw new IndexBuildException("索引【"+index+"】正在建立");
        }
        //索引建立已经完成，等待1s
        if(indexInfo!=null && indexInfo.getDataIndexedState()){
            while(true){
                Thread.sleep(1000);
                indexInfo = indexBuildQueueManager.getCache(index);
                //等待了1s后，发现还是完成状态，可能哪出现了问题
                if(indexInfo!=null && indexInfo.getDataIndexedState()){
                    throw new IndexBuildException("索引【"+index+"】建立出现问题");
                }
            }
        }
        IndexConfigBean indexConfig = indexConfigFactory.getIndexConfig(index);

        ResultSetHandler queryHandler = resultSetHandlerFactory.getQueryHandler(index,indexBuildQueueManager);


        if(indexConfig==null|| queryHandler==null){
            throw new IndexBuildException("不支持该索引的建立");
        }

        // 索引是否存在，删除索引，创建索引
        if(!indexPreBuildProcessorFactory
                .getIndexPreBuildProcessorChain(esManager,indexConfig)
                .execute()){
            throw new IndexBuildException("索引【"+index+"】预处理失败...");
        }
    }
}
