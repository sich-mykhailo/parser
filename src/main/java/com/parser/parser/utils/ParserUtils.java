package com.parser.parser.utils;

import java.util.Objects;
import java.util.stream.Collectors;

public class ParserUtils {
    public static Integer mapToNumbers(String text) {
        if (Objects.isNull(text) || text.isEmpty()) {
            return -1;
        }
        String completeNumber = text.chars()
                .filter(e -> e >= 48 && e <= 57)
                .mapToObj(c -> (char) c)
                .map(item -> item + "")
                .collect(Collectors.joining());
        if (completeNumber.isEmpty()) {
            return -1;
        }
        return Integer.parseInt(completeNumber);
    }
}