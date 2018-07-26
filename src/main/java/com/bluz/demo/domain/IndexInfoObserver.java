package com.bluz.demo.domain;

import com.bluz.demo.queue.IndexBuildQueueManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.Observable;
import java.util.Observer;

@Component
@Scope(value="prototype",proxyMode = ScopedProxyMode.TARGET_CLASS)
public class IndexInfoObserver implements Observer {

    @Autowired
    private IndexBuildQueueManager indexBuildQueueManager;

    @Override
    public void update(Observable o, Object arg) {
                IndexInfo indexInfo=(IndexInfo)arg;
                indexBuildQueueManager.returnBack(indexInfo.getQueue());
                indexBuildQueueManager.invaildIndexCache(indexInfo.getIndex());
        System.out.println("【"+indexInfo.getIndex()+"】索引已建立完成，归还队列，清除缓存");
    }
}
