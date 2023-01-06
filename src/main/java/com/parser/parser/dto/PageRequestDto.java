package com.parser.parser.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class PageRequestDto {
    private String url;
    private String title;
    private String date;
    private String price;
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