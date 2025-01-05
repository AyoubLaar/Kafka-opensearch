package org.example.OpenSearch;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.Mapper;
import org.opensearch.OpenSearchStatusException;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.common.xcontent.XContentType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@Slf4j
public class CoreEventIndex {

    private static String index_name = "core-event-index";

    private long id;
    private String type;
    private int namespace;
    private String title;
    private String titleUrl;
    private String comment;
    private long timestamp;
    private String user;
    private boolean bot;
    private String notifyUrl;
    private boolean minor;
    private boolean patrolled;

    static {
        createCoreEventIndex();
    }

    public static Map<String,Object> getMapping(){

        Map<String, Object> properties = new HashMap<>();
        // id -> "long"
        Map<String, String> idMapping = new HashMap<>();
        idMapping.put("type", "long");
        properties.put("id", idMapping);
        // type -> "keyword"
        Map<String, String> typeFieldMapping = new HashMap<>();
        typeFieldMapping.put("type", "keyword");
        properties.put("type", typeFieldMapping);
        // namespace -> "integer"
        Map<String, String> namespaceMapping = new HashMap<>();
        namespaceMapping.put("type", "integer");
        properties.put("namespace", namespaceMapping);
        // title -> "text"
        Map<String, String> titleMapping = new HashMap<>();
        titleMapping.put("type", "text");
        properties.put("title", titleMapping);
        // titleUrl -> "keyword"
        Map<String, String> titleUrlMapping = new HashMap<>();
        titleUrlMapping.put("type", "keyword");
        properties.put("titleUrl", titleUrlMapping);
        // comment -> "text"
        Map<String, String> commentMapping = new HashMap<>();
        commentMapping.put("type", "text");
        properties.put("comment", commentMapping);
        // timestamp -> "date" (assuming epoch_millas; adjust if you're using long or a different format)
        Map<String, String> timestampMapping = new HashMap<>();
        timestampMapping.put("type", "date");
        timestampMapping.put("format", "epoch_millis");
        properties.put("timestamp", timestampMapping);
        // user -> "keyword"
        Map<String, String> userMapping = new HashMap<>();
        userMapping.put("type", "keyword");
        properties.put("user", userMapping);
        // bot -> "boolean"
        Map<String, String> botMapping = new HashMap<>();
        botMapping.put("type", "boolean");
        properties.put("bot", botMapping);
        // notifyUrl -> "keyword"
        Map<String, String> notifyUrlMapping = new HashMap<>();
        notifyUrlMapping.put("type", "keyword");
        properties.put("notifyUrl", notifyUrlMapping);
        // minor -> "boolean"
        Map<String, String> minorMapping = new HashMap<>();
        minorMapping.put("type", "boolean");
        properties.put("minor", minorMapping);
        // patrolled -> "boolean"
        Map<String, String> patrolledMapping = new HashMap<>();
        patrolledMapping.put("type", "boolean");
        properties.put("patrolled", patrolledMapping);

        Map<String, Object> mapping = new HashMap<>();
        mapping.put("properties", properties);

        return mapping;
    }

    public static void createCoreEventIndex(){
        Connection connection = new Connection();
        connection.connect();
        try {
            connection.createIndex(index_name, getMapping());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (OpenSearchStatusException e){
            log.info("{} already created",index_name);
        }
    }

    public IndexRequest index(){
        Map<String, Object> doc = Mapper.mapCoreEventToMap(this);
        IndexRequest indexRequest = new IndexRequest(index_name);
        indexRequest.source(doc, XContentType.JSON);
        return indexRequest;
    }

    public void save(){
        Map<String, Object> doc = Mapper.mapCoreEventToMap(this);
        try {
            Connection connection = new Connection(); // or a singleton / injected instance
            connection.connect();
            connection.indexDocument(index_name, doc);
        } catch (IOException e) {
            log.error("Error saving document to OpenSearch", e);
            throw new RuntimeException(e);
        } catch (OpenSearchStatusException e) {
            log.error("OpenSearch rejected the request", e);
            throw e;
        }

    }

}
