package com.parser.parser.utils;

import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Log4j2
public class JsoupConnection {
    public static Document createConnection(String url) {
        try {
            Connection connection = Jsoup.connect(url);
            connection.userAgent("Mozilla");
            connection.cookie("cookiename", "val234");
            connection.cookie("cookiename", "val234");
            connection.referrer("http://google.com");
            connection.header("headersecurity", "xyz123");
            return connection.get();
        } catch (Exception e) {
            log.warn("URL data incorrect! Url: " + url);
        }
        return null;
    }
}