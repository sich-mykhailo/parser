package com.parser.parser.dto;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponseDto {
    Long id;
    String url;
    String title;
    Integer price;
    String date;
    Integer views;
    String startOfWork;
    String oblast;
}