package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.Stream;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Producer<String, String> producer = Producer.<String,String>getProducer();
        try {
            while(true) {
                Stream<String> stream = RequestHandler.getDataStream();
                stream = StreamTransformer.transformStream(stream);
                stream.forEach(jsonString -> {
                    producer.publish(jsonString);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.close();
        }
    }

}
