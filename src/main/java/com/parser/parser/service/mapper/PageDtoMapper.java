package com.parser.parser.service.mapper;

import com.parser.parser.dto.PageRequestDto;
import com.parser.parser.dto.PageResponseDto;
import com.parser.parser.entity.Page;
import com.parser.parser.utils.ParserUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.Objects;

@Component
@Log4j2
public class PageDtoMapper implements RequestDtoMapper<PageRequestDto, Page>,
        ResponseDtoMapper<PageResponseDto, Page> {

    @Override
    public Page mapToModel(PageRequestDto dto) {
        Page page = new com.parser.parser.entity.Page();
        page.setUrl(Objects.nonNull(dto.getUrl()) ? dto.getUrl() : "-");
        page.setViews(ParserUtils.mapToNumbers(dto.getViews()));
        page.setTitle(Objects.nonNull(dto.getTitle()) ? dto.getTitle() : "-");
        //page.setRegistrationDate(Objects.nonNull(dto.getDate()) ? buildDate(dto.getDate()) : "-");
        page.setPrice(ParserUtils.mapToNumbers(dto.getPrice()));
       // page.setStartOfWork(Objects.nonNull(dto.getStartOfWork()) ? buildDate(dto.getStartOfWork().substring(13)) : "-");
        page.setOblast(Objects.nonNull(dto.getOblast()) && dto.getOblast().contains("-") ? dto.getOblast().split("-")[1] : "-");
        page.setCity(Objects.nonNull(dto.getCity()) && dto.getCity().contains("-") ? dto.getCity().split("-")[1] : "-");
        page.setDateOfPublication(Objects.nonNull(dto.getDateOfPublication()) ? buildDate(dto.getDateOfPublication().substring(13)) : "-");
        page.setIndividual(Objects.nonNull(dto.getIndividual()) ? dto.getIndividual() : "-");
      //  page.setIsTop(dto.getIsTop() ? "ТОП" : "НЕ ТОП");
        page.setOlxDelivery(dto.isOlxDelivery() ? 1 : 0);
        page.setState(Objects.nonNull(dto.getState()) && dto.getState().contains(":") ? dto.getState().split(":")[1] : "-");
        page.setTitleForTable(Objects.nonNull(dto.getTitleForTable()) ? dto.getTitleForTable() : "-");
        page.setSection(Objects.nonNull(dto.getSection()) ? dto.getSection() : "-");
        return page;
    }

    @Override
    public PageResponseDto mapToDto(Page article) {
        PageResponseDto pageResponseDto = new PageResponseDto();
        pageResponseDto.setId(article.getId());
        pageResponseDto.setViews(article.getViews());
        pageResponseDto.setDate(article.getRegistrationDate());
        pageResponseDto.setUrl(article.getUrl());
        pageResponseDto.setTitle(article.getTitle());
        pageResponseDto.setStartOfWork(article.getStartOfWork());
        pageResponseDto.setOblast(article.getOblast());
        return pageResponseDto;
    }

    private String buildDate(String date) {
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

    private String buildMonth(String date) {
        if (date.contains("січень") || date.contains("січня")) {
            return "01";
        }
        if (date.contains("лютий") || date.contains("лютого")) {
            return "02";
        }
        if (date.contains("березень") || date.contains("березня")) {
            return "03";
        }
        if (date.contains("квітень") || date.contains("квітня")) {
            return "04";
        }
        if (date.contains("травень") || date.contains("травня")) {
            return "05";
        }
        if (date.contains("червень") || date.contains("червня")) {
            return "06";
        }
        if (date.contains("липень") || date.contains("липня")) {
            return "07";
        }
        if (date.contains("серпень") || date.contains("серпня")) {
            return "08";
        }
        if (date.contains("вересень") || date.contains("вересня")) {
            return "09";
        }
        if (date.contains("жовтень") || date.contains("жовтня")) {
            return "10";
        }
        if (date.contains("листопад") || date.contains("листопада")) {
            return "11";
        }
        if (date.contains("грудень") || date.contains("грудня")) {
            return "12";
        }
        return "today";
    }
}