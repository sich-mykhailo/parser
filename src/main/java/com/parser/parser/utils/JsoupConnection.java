package com.parser.parser.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
        } catch (IOException | IllegalArgumentException e) {
            log.warn("Url: " + url,  e);
        }
        return null;
    }
}