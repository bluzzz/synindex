package com.bluz.demo.es;

import com.bluz.demo.domain.IndexBuildResultManager;
import com.bluz.demo.domain.IndexInfo;
import com.bluz.demo.queue.IndexBuildQueueManager;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

import java.util.concurrent.CountDownLatch;

public class BulkProcessorListener implements BulkProcessor.Listener {
    private long amount=0L;
    private IndexBuildResultManager indexBuildResultManager;
    private String index;
    private IndexBuildQueueManager indexBuildQueueManager;

    public static class Builder {
        private BulkProcessorListener bulkProcessorListener=new BulkProcessorListener();

        public static Builder newBuilder() {
            return new Builder();
        }

        public Builder setIndex(String index) {
            bulkProcessorListener.index = index;
            return this;
        }


        public Builder setIndexBuildQueueManager(IndexBuildQueueManager indexBuildQueueManager) {
            bulkProcessorListener.indexBuildQueueManager = indexBuildQueueManager;
            return this;
        }

        public Builder setIndexBuildResultManager(IndexBuildResultManager indexBuildResultManager) {
            bulkProcessorListener.indexBuildResultManager = indexBuildResultManager;
            return this;
        }

        public BulkProcessorListener build(){
            return bulkProcessorListener;
        }

    }

        public String getIndex() {
            return index;
        }

        public IndexBuildQueueManager getIndexBuildQueueManager() {
            return indexBuildQueueManager;
        }

        public IndexBuildResultManager getIndexBuildResultManager() {
            return indexBuildResultManager;
        }

    @Override
        public void beforeBulk(long l, BulkRequest bulkRequest) {
        }
        //成功
        @Override
        public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
            if (bulkResponse.hasFailures()) {
                for (BulkItemResponse item :
                        bulkResponse.getItems()) {
                    if (item.isFailed()) {
                        System.out.println("索引失败信息:" +
                                item.getFailureMessage());
                    }
                }
            }
            amount+=bulkResponse.getItems().length;
            IndexInfo indexInfo = indexBuildQueueManager.getCache(index);
            // 此处不能直接判断数目相等，比如某一刻，生产端数据没有全部生产完，而消费端已经
            // 将生产的数据消费完 虽然相等，但是还未完成所有数据的索引。所以要数据加载完成标志
            System.out.println("【"+index+"】已建立"+amount+"条索引");
            if(indexInfo.getDataLoadedState()&& (indexInfo.getCount()==amount)){
                //不能在此处将索引状态的变更 放在inform方法中去做，如果都放在inform中
                //1.变更建立索引状态 2.归还队列 3.清除index的map缓存
                //有可能说 这三步都做完后，消费线程才执行，而此判断索引是否建立完成的缓存都已经被清了，造成bug
                //所以先变更状态，线程结束后，再清除缓存等操作
                //indexBuildResultManager.inform(index);
                IndexInfo.Builder.newBuilder(indexInfo).setDataIndexedState(true);
            }
        }
        //失败
        @Override
        public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
            System.out.println("【"+index+"】索引失败，"+throwable.getMessage());
        }
}
