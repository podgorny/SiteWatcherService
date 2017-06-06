package it.devchallange.podgorny.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class DecoderImpl implements Decoder {

    public String getSHA256Hex(String src){
        return DigestUtils.sha256Hex(src).toUpperCase();
    }

    public String getMD5Hex(String src){
        return DigestUtils.md5Hex(src).toUpperCase();
    }
}
