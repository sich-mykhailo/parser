package com.parser.parser.service.mapper;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
class PageMapperImplTest {

    @InjectMocks
    PageMapperImpl pageMapperImpl;

    @Test
    void testBuildMonth() {
        String[] dates = {
                "січень", "лютий", "березень", "квітень", "травень", "червень",
                "липень", "серпень", "вересень", "жовтень", "листопад", "грудень",
                "січня", "лютого", "березня", "квітня", "травня", "червня",
                "липня", "серпня", "вересня", "жовтня", "листопада", "грудня",
                "random text"
        };
        String[] expectedMonths = {
                "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12",
                "01", "02", "03", "04", "05", "06",
                "07", "08", "09", "10", "11", "12",
                "today"
        };

        for (int i = 0; i < dates.length; i++) {
            String result = pageMapperImpl.buildMonth(dates[i]);
            Assertions.assertEquals(expectedMonths[i], result);
        }
    }
}
