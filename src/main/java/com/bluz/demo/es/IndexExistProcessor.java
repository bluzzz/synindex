package com.bluz.demo.es;

public class IndexExistProcessor extends AbstractIndexPreBuildProcessor {
    private EsManager esManager;
    private String index;
    public IndexExistProcessor(EsManager esManager,String index) {
        this.esManager=esManager;
        this.index=index;
    }

    @Override
    public Boolean doExecute() throws Exception{
        return esManager.indexExists(index);
    }
}
