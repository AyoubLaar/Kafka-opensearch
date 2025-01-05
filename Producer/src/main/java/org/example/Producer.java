package org.example;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@AllArgsConstructor
/*
* Handle Kafka producer config and publishing
* */
public class Producer<k,V> {

    private KafkaProducer<k,V> kafkaProducer;

    private static String bootstrapServers = "";
    private static String topic = "";

    static {
        try (InputStream input = Producer.class.getClassLoader().getResourceAsStream("application.properties")) {
            Properties properties = new Properties();
            if (input == null) {
                throw new NullPointerException("Unable to find file application.properties");
            }
            properties.load(input);
            bootstrapServers = (String)properties.get("kafka.bootstrap.servers");
            topic = (String) properties.get("kafka.topic");
            log.info("topic : {} , bootstrap servers : {}",topic,bootstrapServers);
        } catch (IOException e) {
            throw new RuntimeException(e.getCause()+" "+e.getMessage());
        }
    }

    public static <keyType extends String,valueType> Producer<keyType,valueType> getProducer(){
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<keyType, valueType> kafkaProducer = new KafkaProducer<>(properties);
        return new Producer<>(kafkaProducer);
    }

    public void publish(k key, V value){
        ProducerRecord<k, V> record = new ProducerRecord<>(topic,key,value);
        kafkaProducer.send(record,((recordMetadata, e) -> {
            if(e == null) log.info(key+" sent successfully.");
            else log.info(key+" failed to send.");
        }));
        kafkaProducer.flush();
    }

    public void publish(V value){
        ProducerRecord<k, V> record = new ProducerRecord<>(topic,value);
        kafkaProducer.send(record,((recordMetadata, e) -> {
            if(e == null) log.info("message sent successfully.");
            else log.info("message failed to send.");
        }));
        kafkaProducer.flush();
    }

    public void close(){
        if(kafkaProducer != null) {
            kafkaProducer.close();
            kafkaProducer = null;
        }
    }

}
