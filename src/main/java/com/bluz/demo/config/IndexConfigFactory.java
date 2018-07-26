package com.bluz.demo.config;

import com.bluz.demo.domain.Index;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IndexConfigFactory {

    //项目
    @Value("${project.index}")
    private String projectIndex;
    @Value("${project.type}")
    private String projectType;
    @Value("${project.query.sql}")
    private String projectQuerySql;
    @Value("${project.index.mapping}")
    private String projectIndexMapping;

    //机构
    @Value("${org.index}")
    private String orgIndex;
    @Value("${org.type}")
    private String orgType;
    @Value("${org.query.sql}")
    private String orgQuerySql;
    @Value("${org.index.mapping}")
    private String orgIndexMapping;

    //事件
    @Value("${event.index}")
    private String eventIndex;
    @Value("${event.type}")
    private String eventType;
    @Value("${event.query.sql}")
    private String eventQuerySql;
    @Value("${event.index.mapping}")
    private String eventIndexMapping;


    public IndexConfigBean getIndexConfig(String index){
        IndexConfigBean indexConfigBean = new IndexConfigBean();
        if(Index.PROJECT.equals(index)){
            indexConfigBean.setIndex(projectIndex);
            indexConfigBean.setIndexType(projectType);
            indexConfigBean.setQuerySql(projectQuerySql);
            indexConfigBean.setIndexMapping(projectIndexMapping);
        }else if(Index.EVENT.equals(index)){
            indexConfigBean.setIndex(eventIndex);
            indexConfigBean.setIndexType(eventType);
            indexConfigBean.setQuerySql(eventQuerySql);
            indexConfigBean.setIndexMapping(eventIndexMapping);
        }else if(Index.ORG.equals(index)){
            indexConfigBean.setIndex(orgIndex);
            indexConfigBean.setIndexType(orgType);
            indexConfigBean.setQuerySql(orgQuerySql);
            indexConfigBean.setIndexMapping(orgIndexMapping);
        }else{
            return null;
        }
        return indexConfigBean;
    }
}
