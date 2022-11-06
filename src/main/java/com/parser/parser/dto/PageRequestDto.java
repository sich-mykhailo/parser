package com.parser.parser.dto;

import lombok.Data;

@Data
public class PageRequestDto {
    private String url;
    private String title;
    private String price;
    private String date;
    private String views;
    private String orderId;
    private String startOfWork;
    private String oblast;
    private String section;
    private String city;
    private String dateOfPublication;
    private String titleForTable;
    private Boolean isTop;
    private String individual;
    private String state;
    private boolean olxDelivery;
}