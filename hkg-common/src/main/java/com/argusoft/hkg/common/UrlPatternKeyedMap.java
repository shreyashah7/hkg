package com.argusoft.hkg.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Iterator;
import org.springframework.util.AntPathMatcher;

/**
 * This map implementation uses a hash map as the underlying storage. The put()
 * method uses a ant path pattern or actual path as a key. The get() method gets
 * any value that matches one of the ant path pattern.
 *
 * @author kelvin
 */
public class UrlPatternKeyedMap extends HashMap<Object, Object> {

    @Override
    public Object put(Object key, Object value) {
        String keyString = (String) key;
        if (key instanceof String) {
            return super.put(keyString, value);
        } else {
            throw new RuntimeException("UrlPatternKeyedMap - only accepts String as a Key...");
        }
    }

    @Override
    public Object get(Object key) {
        Iterator urlPatterns = keySet().iterator();
        String keyString;
        Object result = null;
        String stringToMatch = key.toString();
        boolean isMatched = false;
        AntPathMatcher antPathMatcher = new AntPathMatcher();

        while (urlPatterns.hasNext()) {
            keyString = urlPatterns.next().toString();
            isMatched = antPathMatcher.match(keyString, stringToMatch);
            if (isMatched) {
                result = super.get(keyString);
                break;
            }
        }
        return result;
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    public static UrlPatternKeyedMap fromJson(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, UrlPatternKeyedMap.class);
    }
}
