package com.parser.parser.service;

import com.parser.parser.dto.Page;

import java.util.List;

public interface ParserService {

    List<Page> getAllItems(List<Page> items);
}
