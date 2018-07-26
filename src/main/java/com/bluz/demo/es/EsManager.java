package com.bluz.demo.es;

import com.bluz.demo.domain.IndexInfo;
import com.bluz.demo.queue.IndexBuildQueueManager;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * es 管理组件
 */
@Component
public class EsManager {
    private static final Logger logger= LoggerFactory.getLogger(EsManager.class);
    //集群名称
    @Value("${cluster.name}")
    private String clusterName;
    //地址
    @Value("${cluster.host}")
    private String clusterHost;
    //端口
    @Value("${cluster.port}")
    private Integer clusterPort;

    /**
     * es 客户端
     * @return
     * @throws Exception
     */
    public TransportClient getClient() throws Exception{
        Settings settings = Settings.builder()
                .put("cluster.name", clusterName)
                .build();

        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(clusterHost), clusterPort));

        return client;
    }

    /**
     * 关闭资源
     * @param client
     * @throws Exception
     */
    public void close(TransportClient client) throws Exception{
        client.close();
    }

    /**
     * 判断索引是否存在
     * @param index
     * @return
     * @throws Exception
     */
    public Boolean indexExists(String index) throws Exception{
        TransportClient client = getClient();
        IndicesExistsResponse indicesExistsResponse = client
                .admin()
                .indices()
                .exists(new IndicesExistsRequest(index))
                .actionGet();
        Boolean isExists=indicesExistsResponse.isExists();
        client.close();
        return isExists;
    }

    /**
     * 创建索引
     * @param index 索引库
     * @param type  索引类型
     * @param source 映射
     * @throws Exception
     */
    public Boolean createIndex(String index,String type,String source) throws Exception{
        TransportClient client = getClient();
        CreateIndexResponse createIndexResponse = client
                .admin()
                .indices()
                .prepareCreate(index)
                .addMapping(type, source)
                .get();
        Boolean acknowledged = createIndexResponse.isAcknowledged();
        client.close();
        return acknowledged;
    }

    /**
     * 删除索引
     * @param index
     * @return
     * @throws Exception
     */
    public Boolean delIndex(String index) throws Exception{
        TransportClient client = getClient();
        DeleteIndexResponse deleteIndexResponse = client.admin().indices().prepareDelete(index).execute().actionGet();
        Boolean flag=deleteIndexResponse.isAcknowledged();
        client.close();
        return flag;
    }


    public BulkProcessor build(BulkProcessorListener processorListener) throws Exception{
        BulkProcessor bulkProcessor=BulkProcessor.builder(getClient(), processorListener)
                .setBulkActions(30000)
                .setBulkSize(new ByteSizeValue(10, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5)).build();
        return bulkProcessor;
    }
}
