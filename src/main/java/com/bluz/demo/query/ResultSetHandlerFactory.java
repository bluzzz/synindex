package com.bluz.demo.query;

import com.bluz.demo.domain.Index;
import com.bluz.demo.domain.IndexInfo;
import com.bluz.demo.queue.IndexBuildQueueManager;
import org.springframework.stereotype.Component;

@Component
public class ResultSetHandlerFactory {
    public AbstractResultSetHandler getQueryHandler(String  index,
                                                    IndexBuildQueueManager indexBuildQueueManager){
        if(Index.PROJECT.equals(index)){
            return new ProjectResultSetHandler(index,indexBuildQueueManager);
        }else if(Index.EVENT.equals(index)){
            return new EventResultSetHandler(index,indexBuildQueueManager);
        }else if(Index.ORG.equals(index)){
            return new OrgResultSetHandler(index,indexBuildQueueManager);
        }else{
            return null;
        }
    }
}
