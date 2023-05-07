package com.parser.parser.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class    Page {
     Long id;
     String url;
     String title;
     String price;
     String registrationDate;
     String views;
     String orderId;
     String startOfWork;
     String oblast;
     String section;
     String dateOfPublication;
     String top;
     String individual;
     String state;
     String olxDelivery;

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
