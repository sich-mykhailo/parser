package com.parser.parser.dto;

import lombok.Data;

@Data
public class PageResponseDto {
    private Long id;
    private String url;
    private String title;
    private Integer price;
    private String date;
    private Integer views;
    private String startOfWork;
    private String oblast;
}