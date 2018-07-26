package com.bluz.demo.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class IndexBuildResultManager {
    @Autowired
    private IndexInfoObserver indexInfoObserver;

    private ConcurrentHashMap<String,IndexInfoObservable> indexInfoObserverMap=new ConcurrentHashMap<>();

    public void observe(IndexInfo indexInfo){
        IndexInfoObservable indexInfoObservable = new IndexInfoObservable(indexInfo);
        System.out.println("indexInfoObserver:"+indexInfoObserver);
        indexInfoObservable.addObserver(indexInfoObserver);
        indexInfoObserverMap.put(indexInfo.getIndex(),indexInfoObservable);
    }

    public void inform(String index){
        IndexInfoObservable indexInfoObservable = indexInfoObserverMap.get(index);
        indexInfoObservable.finish();
        indexInfoObserverMap.remove(index);
    }
}
