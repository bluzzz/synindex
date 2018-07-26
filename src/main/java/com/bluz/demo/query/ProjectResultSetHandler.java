package com.bluz.demo.query;

import com.alibaba.fastjson.JSON;
import com.bluz.demo.domain.IndexInfo;
import com.bluz.demo.queue.IndexBuildQueueManager;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ProjectResultSetHandler extends AbstractResultSetHandler {

    public ProjectResultSetHandler(String index, IndexBuildQueueManager indexBuildQueueManager) {
        super(index, indexBuildQueueManager);
    }

    @Override
    public void handle(ResultSet resultSet) throws Exception{
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        long count=0L;
        IndexBuildQueueManager indexBuildQueueManager = getIndexBuildQueueManager();
        String index = getIndex();
        IndexInfo indexInfo= indexBuildQueueManager.getCache(index);
        ConcurrentLinkedQueue<String> queue = indexInfo.getQueue();
        while(resultSet.next()){
            HashMap<String, Object> map = new HashMap<>();
            for (int i = 1; i <= columnCount ; i++) {
                String columnName = metaData.getColumnName(i);
                Object columnVal = resultSet.getObject(i);
                if("industryIds".equals(columnName)&&!StringUtils.isEmpty(columnVal)){
                    List<String> industryIds = new ArrayList<String>();
                    String[] ids = columnVal.toString().split(",");
                    for (String id : ids) {
                        industryIds.add(id);
                    }
                    map.put(columnName,industryIds);
                }
                map.put(columnName, StringUtils.isEmpty(columnVal)?null:columnVal);
            }
            String data = JSON.toJSONString(map);
            queue.add(data);
            count++;
            if(count%50000==0){
                System.out.println("【"+index+"】已加载"+count+"条数据");
            }
        }
        // 数据已加载完成
        System.out.println("【"+index+"】已加载"+count+"条数据");
        indexInfo = IndexInfo.Builder.newBuilder(indexInfo).setCount(count).setDataLoadedState(true).build();
        System.out.println("【"+index+"】加载状态"+indexInfo.getDataLoadedState());
        indexBuildQueueManager.putIndexCache(index, indexInfo);
    }
}
