package com.bluz.demo.es;

import com.bluz.demo.config.IndexConfig;
import com.bluz.demo.config.IndexConfigBean;
import org.springframework.stereotype.Component;

@Component
public class IndexPreBuildProcessorFactory {

    public IndexPreBuildProcessor getIndexPreBuildProcessorChain(EsManager esManager, IndexConfigBean indexConfig){

        String index = indexConfig.getIndex();
        String indexType = indexConfig.getIndexType();
        String indexMapping = indexConfig.getIndexMapping();
        IndexExistProcessor indexExistProcessor = new IndexExistProcessor(esManager, index);

        IndexDelProcessor indexDelProcessor = new IndexDelProcessor(esManager, index);
        IndexCreateProcessor indexCreateProcessor = new IndexCreateProcessor(index, indexType, indexMapping, esManager);

        indexExistProcessor.setSuccessProcessor(indexDelProcessor);
        indexExistProcessor.setErrorProcessor(indexCreateProcessor);

        indexDelProcessor.setSuccessProcessor(indexCreateProcessor);

        return indexExistProcessor;
    }
}
