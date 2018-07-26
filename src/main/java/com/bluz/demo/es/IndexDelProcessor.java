package com.bluz.demo.es;

public class IndexDelProcessor extends AbstractIndexPreBuildProcessor {
    private EsManager esManager;
    private String index;
    public IndexDelProcessor(EsManager esManager,String index) {
        this.esManager=esManager;
        this.index=index;
    }
    @Override
    Boolean doExecute() throws Exception {
        return esManager.delIndex(index);
    }
}
