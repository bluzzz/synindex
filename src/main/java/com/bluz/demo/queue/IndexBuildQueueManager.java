package com.bluz.demo.queue;

import com.bluz.demo.domain.IndexInfo;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;


@Component
public class IndexBuildQueueManager {

    private static Integer QUEUE_NUM=10;

    private ConcurrentHashMap<String,IndexInfo> runningIndexCache=new ConcurrentHashMap<>();

    private LinkedList<ConcurrentLinkedQueue<String>> queueList=new LinkedList<>();

    /**
     * 队列池
     */
    private IndexBuildQueueManager(){
        for (int i = 0; i <QUEUE_NUM; i++) {
            ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
            queueList.push(queue);
        }
    }

    private static class Singleton{
        private static IndexBuildQueueManager indexBuildQueueManager;
        static{
            indexBuildQueueManager =new IndexBuildQueueManager();
        }
        private static IndexBuildQueueManager getInstance(){
            return indexBuildQueueManager;
        }
    }

    public static IndexBuildQueueManager getInstance(){
        return Singleton.getInstance();
    }

    public void add(ConcurrentLinkedQueue<String> queue){
        queueList.add(queue);
    }

    public synchronized  ConcurrentLinkedQueue<String> getQueue(){
        while(queueList.size()==0){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ConcurrentLinkedQueue<String> queue = queueList.poll();
        return queue;
    }

    public void returnBack(ConcurrentLinkedQueue<String> queue){
        queueList.push(queue);
    }

    public int getQueueSize(){
        return queueList.size();
    }

    public void putIndexCache(String index,IndexInfo indexInfo){
        runningIndexCache.put(index,indexInfo);
    }

    public void invaildIndexCache(String index){
        runningIndexCache.remove(index);
    }

    public IndexInfo getCache(String index){
        return runningIndexCache.get(index);
    }

}
