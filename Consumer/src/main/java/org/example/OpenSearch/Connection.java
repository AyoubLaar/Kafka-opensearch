package org.example.OpenSearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.example.Consumer;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.bulk.BulkResponse;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.client.indices.CreateIndexRequest;
import org.opensearch.client.indices.CreateIndexResponse;
import org.opensearch.common.settings.Settings;
import org.opensearch.common.xcontent.XContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class Connection {

    private static final String username;
    private static final String password;
    private static final String opensearchHost;
    private static final String opensearchScheme;
    private static final int opensearchPort;

    private RestHighLevelClient client;

    static {
        try(InputStream inputStream = Consumer.class.getClassLoader().getResourceAsStream("application.properties")){

            Properties properties = new Properties();
            properties.load(inputStream);

            username = (String)properties.get("opensearch.username");
            password = (String)properties.get("opensearch.password");
            opensearchHost = (String)properties.get("opensearch.host");
            opensearchPort = Integer.parseInt((String)properties.get("opensearch.port"));
            opensearchScheme = (String)properties.get("opensearch.scheme");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RestHighLevelClient connect(){
        //Create a client.
        RestClientBuilder builder = RestClient.builder(new HttpHost(opensearchHost, opensearchPort, opensearchScheme));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        log.info("connected successfully to opensearch.");
        setClient(client);
        return client;
    }

    public void createIndex(String index, Map<String, Object> schema) throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);
        createIndexRequest.settings(Settings.builder() //Specify in the settings how many shards you want in the index.
                .put("index.number_of_shards", 4)
                .put("index.number_of_replicas", 3)
        );
        createIndexRequest.mapping(schema);
        CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        log.info("created {} successfully!",index);
    }


    public void setClient(RestHighLevelClient client){
        if(this.client != null) {
            try {
                this.client.close();
            } catch (IOException e) {
                System.out.println("client is already closed.");
            }
        }
        this.client = client;
    }


    public void indexDocument(String indexName, Map<String, Object> doc) throws IOException {
        IndexRequest request = new IndexRequest(indexName);
        request.source(doc, XContentType.JSON);
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        log.info("Indexed document with status: " + response.getResult());
    }

    public void send(BulkRequest bulkRequest) throws IOException {
        BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        log.info("Indexed document with status: " + response.status());
    }

}
