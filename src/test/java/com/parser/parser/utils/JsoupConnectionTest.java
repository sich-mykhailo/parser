package com.parser.parser.utils;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JsoupConnectionTest {

    @Test
    void shouldReturnObject() {
        Document connection = JsoupConnection.createConnection("https://www.olx.ua/uk/");
        Assertions.assertNotNull(connection);
    }

    @Test
    void checkWithWrongUrl() {
        Document connection = JsoupConnection.createConnection("wrong url");
        Assertions.assertNull(connection);
    }

    @Test
    void checkWithUnreachableUrl() {
        Document connection = JsoupConnection.createConnection("http://test-example.com");
        Assertions.assertNull(connection);
    }
}
