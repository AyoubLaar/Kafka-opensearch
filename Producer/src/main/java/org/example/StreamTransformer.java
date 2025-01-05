package org.example;
import java.util.stream.Stream;

public class StreamTransformer {

    public static Stream<String> transformStream(Stream<String> stream) {
        return stream
                .map(line -> line.split("\n"))
                .filter(x -> x.length > 0 && x[0].startsWith("data"))
                .map(x -> x[0].substring(6));
    }

}

