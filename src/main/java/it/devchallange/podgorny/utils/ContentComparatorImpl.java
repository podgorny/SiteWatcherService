package it.devchallange.podgorny.utils;

import org.springframework.beans.factory.annotation.Autowired;

public class ContentComparatorImpl implements ContentComparator{
    @Autowired
    private DecoderImpl decoder;

    public boolean compare(String src, String dst) {
        String srcSHA256Hex = decoder.getSHA256Hex(src),
                srcMD5Hex = decoder.getMD5Hex(src),
                dstSHA256Hex = decoder.getSHA256Hex(dst),
                dstMD5Hex = decoder.getMD5Hex(dst);

        return srcMD5Hex.equals(dstMD5Hex) && srcSHA256Hex.equals(dstSHA256Hex);
    }
}
