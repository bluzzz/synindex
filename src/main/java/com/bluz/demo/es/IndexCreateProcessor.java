package com.bluz.demo.es;

public class IndexCreateProcessor extends AbstractIndexPreBuildProcessor {
    private String index;
    private String indexType;
    private String indexMapping;
    private EsManager esManager;
    public IndexCreateProcessor(String index, String indexType, String indexMapping,EsManager esManager) {
        this.index = index;
        this.indexType = indexType;
        this.indexMapping = indexMapping;
        this.esManager=esManager;
    }

    @Override
    Boolean doExecute() throws Exception {
        return esManager.createIndex(index,indexType,indexMapping);
    }
}
