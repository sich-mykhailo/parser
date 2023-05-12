package com.parser.parser.service;

import java.io.ByteArrayInputStream;

public interface PageService {

    ByteArrayInputStream parsePage(String url);
}
