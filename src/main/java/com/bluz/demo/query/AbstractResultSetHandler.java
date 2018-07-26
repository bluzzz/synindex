package com.bluz.demo.query;

import com.bluz.demo.domain.IndexInfo;
import com.bluz.demo.queue.IndexBuildQueueManager;

import java.util.concurrent.ArrayBlockingQueue;

public abstract  class AbstractResultSetHandler implements ResultSetHandler{
    private String index;

    private IndexBuildQueueManager indexBuildQueueManager;

    public AbstractResultSetHandler(String index,IndexBuildQueueManager indexBuildQueueManager) {
        this.index=index;
        this.indexBuildQueueManager=indexBuildQueueManager;
    }


    public String  getIndex() {
        return index;
    }

    public IndexBuildQueueManager getIndexBuildQueueManager() {
        return indexBuildQueueManager;
    }
}
