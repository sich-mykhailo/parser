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
public class PageRequestDto {
    String url;
    String title;
    String date;
    String price;
    String views;
    String orderId;
    String startOfWork;
    String oblast;
    String section;
    String city;
    String dateOfPublication;
    String titleForTable;
    Boolean isTop;
    String individual;
    String state;
    boolean olxDelivery;
}