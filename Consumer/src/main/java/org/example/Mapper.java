package org.example;

import org.example.OpenSearch.CoreEventIndex;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Mapper {

    public static JSONObject mapStringToJSON(String json){
        return new JSONObject(json);
    }

    public static CoreEventIndex mapJsonToIndex(JSONObject json){
        return CoreEventIndex.builder()
                .id(json.optLong("id", 0L))
                .type(json.optString("type", ""))
                .namespace(json.optInt("namespace", 0))
                .title(json.optString("title", ""))
                .titleUrl(json.optString("title_url", ""))    // Adjust the key based on your JSON
                .comment(json.optString("comment", ""))
                .timestamp(json.optLong("timestamp", 0L))
                .user(json.optString("user", ""))
                .bot(json.optBoolean("bot", false))
                .notifyUrl(json.optString("notify_url", "")) // Adjust the key based on your JSON
                .minor(json.optBoolean("minor", false))
                .patrolled(json.optBoolean("patrolled", false))
                .build();
    }

    public static Map<String, Object> mapCoreEventToMap(CoreEventIndex eventIndex){
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", eventIndex.getId());
        doc.put("type", eventIndex.getType());
        doc.put("namespace", eventIndex.getNamespace());
        doc.put("title", eventIndex.getTitle());
        doc.put("titleUrl", eventIndex.getTitleUrl());
        doc.put("comment", eventIndex.getComment());
        doc.put("timestamp", eventIndex.getTimestamp());
        doc.put("user", eventIndex.getUser());
        doc.put("bot", eventIndex.isBot());
        doc.put("notifyUrl", eventIndex.getNotifyUrl());
        doc.put("minor", eventIndex.isMinor());
        doc.put("patrolled", eventIndex.isPatrolled());
        return doc;
    }

}
