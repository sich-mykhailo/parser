package com.parser.parser.service.mapper;

public interface ResponseDtoMapper<R, M> {
    R mapToDto(M t);
}