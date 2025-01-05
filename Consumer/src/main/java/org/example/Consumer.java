package org.example;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

@Slf4j
@AllArgsConstructor
public class Consumer<K extends String,V> {

    private KafkaConsumer<K,V> kafkaConsumer;
    private static final String bootstrapServers;
    private static final String topic;
    private static final String groupId;

    static{
        try(InputStream inputStream = Consumer.class.getClassLoader().getResourceAsStream("application.properties")){

            Properties properties = new Properties();
            properties.load(inputStream);

            bootstrapServers = (String)properties.get("kafka.bootstrap.servers");
            topic = (String)properties.get("kafka.topic");
            groupId = (String)properties.get("kafka.group.id");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <keyType extends String,valueType>Consumer<keyType,valueType> getConsumer(){
        Properties properties = new Properties();

        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        KafkaConsumer<keyType,valueType> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));

        return new Consumer<keyType,valueType>(kafkaConsumer);
    }

    public ConsumerRecords<K,V> consume(int duration){
        ConsumerRecords<K, V> records = kafkaConsumer.poll(Duration.ofMillis(duration));
        return records;
    }

}
