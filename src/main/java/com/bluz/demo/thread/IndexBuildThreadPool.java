package com.bluz.demo.thread;

import com.bluz.demo.queue.IndexBuildQueueManager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 初始化 建立所用的线程池
 */
public class IndexBuildThreadPool {

    private ExecutorService threadPool= Executors.newCachedThreadPool();

    private IndexBuildThreadPool(){

    }

    public void submit(Runnable runnable){
        threadPool.submit(runnable);
    }

    private static class Singleton{

        private static IndexBuildThreadPool indexBuildThreadPool=null;

        static{
            indexBuildThreadPool=new IndexBuildThreadPool();
        }

        private static IndexBuildThreadPool getInstance(){
            return indexBuildThreadPool;
        }
    }

    public static IndexBuildThreadPool getInstance(){
        return Singleton.getInstance();
    }

    public static void init(){
        getInstance();
    }
}
