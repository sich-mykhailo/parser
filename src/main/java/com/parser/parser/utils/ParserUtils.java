package com.parser.parser.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import java.util.Objects;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ParserUtils {
    public static Integer mapToNumbers(String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            return -1;
        }
        String completeNumber = text.replaceAll("\\D+", "");
        if (completeNumber.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(completeNumber);
    }
}
