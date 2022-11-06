package com.parser.parser.service.mapper;

public interface RequestDtoMapper<R, M> {
    M mapToModel(R dto);
}