package org.example;

import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

@Slf4j
public class RequestHandler {

    private static final String url = "https://stream.wikimedia.org/v2/stream/recentchange";

    public static Stream<String> getDataStream(){
        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder().uri(new URI(url)).GET().build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        log.info("request created");
        try{
            HttpClient client = HttpClient.newHttpClient();
            log.info("client created");
            HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());
            log.info("response received");
            return response.body();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
