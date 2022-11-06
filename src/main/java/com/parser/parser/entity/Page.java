package com.parser.parser.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Getter
@Setter
public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String title;
    private Integer price;
    private String registrationDate;
    private Integer views;
    private String orderId;
    private String startOfWork;
    private String oblast;
    private String section;
    private String city;
    private String dateOfPublication;
    private String titleForTable;
    private String isTop;
    private String individual;
    private String state;
    private Integer olxDelivery;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(url, page.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
