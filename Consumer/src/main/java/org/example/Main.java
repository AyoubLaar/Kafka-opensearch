package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.example.OpenSearch.Connection;
import org.example.OpenSearch.CoreEventIndex;
import org.json.JSONObject;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.index.IndexRequest;

import java.io.IOException;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Consumer<String,String> consumer = Consumer.<String ,String>getConsumer();
        Connection connection = new Connection();
        connection.connect();
        while (true) {
            ConsumerRecords<String,String> records = consumer.consume(5000);
            log.info("Processing {} records.",records.count());
            BulkRequest bulkRequest = new BulkRequest();
            for(ConsumerRecord<String,String> record : records) {
                String jsonString = record.value();
                JSONObject jsonObject = Mapper.mapStringToJSON(jsonString);
                CoreEventIndex eventIndexObj = Mapper.mapJsonToIndex(jsonObject);
                IndexRequest request = eventIndexObj.index();
                bulkRequest.add(request);
            }
            try {
                connection.send(bulkRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}