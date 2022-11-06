package com.parser.parser.service;


import com.parser.parser.dto.PageRequestDto;
import com.parser.parser.entity.Page;

import java.util.List;

public interface ParserService {

    List<Page> getAllItems(List<PageRequestDto> items);
}
