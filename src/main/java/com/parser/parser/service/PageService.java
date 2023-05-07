package com.parser.parser.service;

import com.parser.parser.dto.Page;
import java.util.List;

public interface PageService {

    void parsePage(String url);

     List<Page> getAllItemsFromMainUrl(String mainUrl);
}
