package com.parser.parser.service;

import com.parser.parser.dto.PageRequestDto;

import java.util.List;

public interface PageService {

    void parsePage(String url);

     List<PageRequestDto> getAllItemsFromMainUrl(String mainUrl);
}
