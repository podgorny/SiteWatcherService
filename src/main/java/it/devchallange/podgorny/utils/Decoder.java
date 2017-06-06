package it.devchallange.podgorny.utils;

public interface Decoder {
    String getSHA256Hex(String src);
    String getMD5Hex(String src);
}
