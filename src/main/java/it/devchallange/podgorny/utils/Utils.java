package it.devchallange.podgorny.utils;

import it.devchallange.podgorny.dao.jdbc.utils.DBCache;
import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Logger logger = Logger.getLogger(DBCache.class);

    public static boolean isMatchedRegexp(String src, String pattern) {
        if (logger.isTraceEnabled()) {
            logger.trace("src = " + src + ", pattern = " + pattern);
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(src.trim());

        return m.matches();
    }

    public static String regexpReplace(String src, String pattern){
        if (logger.isTraceEnabled()) {
            logger.trace("src = " + src + ", pattern = " + pattern);
        }
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(src);

        return m.toString();
    }
}