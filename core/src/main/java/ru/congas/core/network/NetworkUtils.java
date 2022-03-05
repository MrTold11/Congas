package ru.congas.core.network;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr_Told
 */
public class NetworkUtils {

    public static Map<String, String> queryToMap(String query) {
        if (query == null) return null;

        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            } else {
                result.put(entry[0], "");
            }
        }
        return result;
    }

}
