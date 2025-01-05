package org.example;

import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {

    public static void main(String[] args) {
        Producer<String, String> producer = Producer.<String,String>getProducer();
        try {
            Stream<String> stream = RequestHandler.getDataStream();
            stream = StreamTransformer.transformStream(stream);
            stream.forEach(message -> producer.publish(message));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            producer.close();
        }
    }

}
