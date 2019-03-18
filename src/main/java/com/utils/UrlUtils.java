package com.utils;

import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class UrlUtils {

    public static String encodeStr(String keyword) {
        try {
            return URLEncoder.encode(keyword, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("%21", "!")
                    .replaceAll("%27", "'")
                    .replaceAll("%28", "(")
                    .replaceAll("%29", ")")
                    .replaceAll("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encode string : " + keyword + " error", e);
        }
    }

}
