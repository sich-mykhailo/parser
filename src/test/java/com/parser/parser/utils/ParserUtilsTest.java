package com.parser.parser.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.parser.parser.utils.ParserUtils.mapToNumbers;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ParserUtilsTest {

    @Test
    void testMapToNumbers() {
        assertEquals(12345, mapToNumbers("abc12345def"));
        assertEquals(-1, mapToNumbers("abcdef"));
        assertEquals(6789, mapToNumbers("6789abc"));
        assertEquals(-1, mapToNumbers(""));
        assertEquals(-1, mapToNumbers(null));
        assertEquals(1234567890, mapToNumbers("123a456b7890c"));
    }
}