package it.devchallange.podgorny.http;

import java.util.Map;

public interface SimpleSite {
    String getURLString();
    Map<String, String> getReplacementMap();
}
