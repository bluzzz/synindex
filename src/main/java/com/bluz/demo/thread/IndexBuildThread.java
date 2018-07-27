package com.bluz.demo.thread;

import com.bluz.demo.config.IndexConfigBean;
import com.bluz.demo.domain.IndexBuildResultManager;
import com.bluz.demo.es.BulkProcessorListener;
import com.bluz.demo.queue.IndexBuildQueueManager;
import com.bluz.demo.es.EsManager;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 建立索引线程
 */
public class IndexBuildThread implements Runnable {
    private ConcurrentLinkedQueue<String> queue;
    private EsManager esManager;
    private IndexConfigBean indexConfig;
    private IndexBuildQueueManager indexBuildQueueManager;
    private IndexBuildResultManager indexBuildResultManager;
    public IndexBuildThread(IndexBuildResultManager indexBuildResultManager,
                            IndexBuildQueueManager indexBuildQueueManager,
                            ConcurrentLinkedQueue<String> queue,
                            IndexConfigBean indexConfig,
                            EsManager esManager) {
        this.indexBuildResultManager=indexBuildResultManager;
        this.indexBuildQueueManager=indexBuildQueueManager;
        this.queue=queue;
        this.esManager=esManager;
        this.indexConfig=indexConfig;
        
    }

    @Override
    public void run() {
        try {
            String index = indexConfig.getIndex();
            String indexType = indexConfig.getIndexType();
            BulkProcessorListener processorListener = BulkProcessorListener.Builder.newBuilder()
                    .setIndex(index)
                    .setIndexBuildQueueManager(indexBuildQueueManager)
                    .build();
            BulkProcessor bulkProcessor = esManager.build(processorListener);

            /**
             * 1.此处不能用queue.size来判断，因为可能生产速度过慢，而消费速度过快，这样会导致数据还未生产结束
             就停止消费，造成数据丢失
             *
             * 2.想到再索引结束后通过标识来控制，有如下情况：
             * 比如数据全部生产完，队列中的数据也全部消费完，但是索引还未建立完成，此时标志还是未false
             * 程序再往下走，因为使用的是阻塞队列，所以会hang住，导致无法再次循环，即使索引建立完成，标志设为true
             * 此线程也无法中止，慢慢会造成线程堆积，或者使用非阻塞方法
             *
             * 所以使用非阻塞队列+标识
             */
            while (true){
                    String data = queue.poll();
                    if(indexBuildQueueManager.getCache(index).getDataIndexedState()){
                        break;
                    }
                    if(data==null||"".equals(data)){
                        Thread.sleep(200);
                        continue;
                    }
                    bulkProcessor.add(new IndexRequest(index, indexType).source(data));
            }
            indexBuildResultManager.inform(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
