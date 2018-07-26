package com.bluz.demo.domain;

import java.util.Observable;

public class IndexInfoObservable extends Observable {

    private IndexInfo indexInfo;

    public IndexInfoObservable(IndexInfo indexInfo) {
        this.indexInfo = indexInfo;
    }

    public IndexInfoObservable() {

    }

    public void finish(){
        this.setChanged();
        this.notifyObservers(indexInfo);
    }
}
