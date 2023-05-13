package com.parser.parser.service.mapper;

import com.parser.parser.dto.Page;
import com.parser.parser.utils.ParserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

import static com.parser.parser.utils.Constants.MONTHS_IN_GENITIVE_CASE_UA;
import static com.parser.parser.utils.Constants.MONTHS_IN_NOMINATIVE_CASE_UA;

@Component
@Log4j2
public class PageMapperImpl implements PageMapper {

    @Override
    public Page map(Page dto) {
        Page page = new Page();
        page.setUrl(Objects.nonNull(dto.getUrl()) ? dto.getUrl() : "-");
        page.setViews(ParserUtils.mapToNumbers(dto.getViews()).toString());
        page.setTitle(Objects.nonNull(dto.getTitle()) ? dto.getTitle() : "-");
        page.setRegistrationDate(Objects.nonNull(dto.getRegistrationDate()) ? buildDate(dto.getRegistrationDate()) : "-");
        page.setPrice(ParserUtils.mapToNumbers(dto.getPrice()).toString());
        page.setStartOfWork(Objects.nonNull(dto.getStartOfWork()) ? buildDate(dto.getStartOfWork().substring(13)) : "-");
        page.setDistrict(handleDistrict(dto));
        page.setDateOfPublication(Objects.nonNull(dto.getDateOfPublication()) && dto.getDateOfPublication().length() >= 13
                ? buildDate(dto.getDateOfPublication().substring(13)) : "-");
        page.setIndividual(Objects.nonNull(dto.getIndividual()) ? dto.getIndividual() : "-");
        page.setTop(dto.getTop());
        page.setOlxDelivery(dto.getOlxDelivery());
        page.setState(Objects.nonNull(dto.getState()) && dto.getState().contains(":") ? dto.getState().split(":")[1] : "-");
        page.setSection(Objects.nonNull(dto.getSection()) ? dto.getSection() : "-");
        return page;
    }

    protected String handleDistrict(Page dto) {
        if (Objects.nonNull(dto.getDistrict()) && dto.getDistrict().contains("-")) {
            String result = dto.getDistrict().split("-")[1];
            return result.contains("Івано") ? "Івано-Франківська область" : result;
        }

        return "-";
    }

    protected String buildDate(String date) {
        if (Objects.nonNull(date) && !date.isEmpty()) {
            String currentDate = LocalDate.now().toString();
            if (!date.contains("Сьогодні")) {
                String year;
                String day = date.startsWith("0")
                        || date.startsWith("1")
                        || date.startsWith("2")
                        || date.startsWith("3")
                        ? date.substring(0, 2) : "00";
                String month = buildMonth(date);
                if (day.equals("00")) {
                    year = date.substring(date.length() - 4);
                    return year + "-" + month;
                }
                year = date.substring(date.length() - 7, date.length() - 3);
                return year + "-" + month + "-" + day;
            }
            return currentDate;
        }
        return "-1";
    }

    protected String buildMonth(String date) {
        for (int i = 0; i < MONTHS_IN_NOMINATIVE_CASE_UA.length; i++) {
            if (date.contains(MONTHS_IN_NOMINATIVE_CASE_UA[i])
                    || date.contains(MONTHS_IN_GENITIVE_CASE_UA[i])) {
                return String.format("%02d", i + 1);
            }
        }

        return "today";
    }
}
