package com.bluz.demo.domain;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 索引状态
 */
public class IndexInfo{
    //索引
    private String index;
    //数据加载状态
    private volatile Boolean dataLoadedState;
    //数据索引状态
    private volatile Boolean dataIndexedState;
    //索引条数
    private long count=0L;

    private ConcurrentLinkedQueue<String> queue;

    public  IndexInfo(){
        this.dataLoadedState=false;
        this.dataIndexedState=false;
    }

    public static class Builder{
        private IndexInfo indexInfo;

        public Builder(IndexInfo indexinfo){
            this.indexInfo=indexinfo;
        }

        public static Builder newBuilder(){
            return new IndexInfo.Builder(new IndexInfo());
        }

        public static Builder newBuilder(IndexInfo indexInfo){
            return new IndexInfo.Builder(indexInfo);
        }

        public Builder setQueue(ConcurrentLinkedQueue<String> queue){
            indexInfo.queue=queue;
            return this;
        }
        public Builder setIndex(String index){
            indexInfo.index=index;
            return this;
        }
        public Builder setCount(long count){
            indexInfo.count=count;
            return this;
        }
        public Builder setDataLoadedState(Boolean state) {
            indexInfo.dataLoadedState = state;
            return this;
        }

        public Builder setDataIndexedState(Boolean state){
            indexInfo.dataIndexedState=state;
            return this;
        }

        public IndexInfo build(){
            return indexInfo;
        }
    }

    public ConcurrentLinkedQueue<String> getQueue() {
        return queue;
    }

    public Boolean getDataLoadedState() {
        return dataLoadedState;
    }

    public Boolean getDataIndexedState() {
        return dataIndexedState;
    }

    public long getCount() {
        return count;
    }

    public String getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IndexInfo indexInfo = (IndexInfo) o;
        return Objects.equals(index, indexInfo.index);
    }

    @Override
    public int hashCode() {

        return Objects.hash(index);
    }
}
