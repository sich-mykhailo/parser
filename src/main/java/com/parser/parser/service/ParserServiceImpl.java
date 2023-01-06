package com.parser.parser.service;

import com.parser.parser.dto.PageRequestDto;
import com.parser.parser.entity.Page;
import com.parser.parser.service.mapper.PageDtoMapper;
import com.parser.parser.utils.HtmlClassNames;
import com.parser.parser.utils.JsoupConnection;
import com.parser.parser.utils.ParserUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.net.HttpHeaders.KEEP_ALIVE;
import static com.parser.parser.utils.HtmlClassNames.*;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ParserServiceImpl implements ParserService {
    static String TITLE_TAG = "title";
    static String STATE = "Стан:";
    static String OLX_PAGE_VIEWS_URL = "https://www.olx.ua/api/v1/offers/%s/page-views/";
    static String DEFAULT_PAGE_VIEWS = "0";

    @Value("${olx.token}")
    @NonFinal String olxToken;
    RestTemplate restTemplate;
    PageDtoMapper pageDtoMapper;

    @Override
    public List<Page> getAllItems(List<PageRequestDto> pages) {
        long count = 1L;
        List<Page> completeItems = new ArrayList<>();
        log.info("total size of items: " + pages.size());
        for (PageRequestDto item : pages) {
            if (Objects.nonNull(item.getUrl())) {
                PageRequestDto itemRequestDto = itemParse(item.getUrl());
                if (Objects.nonNull(item.getTitle())) {
                    itemRequestDto.setTitle(item.getTitle());
                }
                itemRequestDto.setUrl(item.getUrl());
                itemRequestDto.setIsTop(item.getIsTop());
                itemRequestDto.setOlxDelivery(item.isOlxDelivery());
                Page completeItem = pageDtoMapper.mapToModel(itemRequestDto);
                completeItem.setId(count++);
                completeItems.add(completeItem);
            }
            if (completeItems.size() % 100 == 0) {
                log.info("completed items: " + completeItems.size());
            }
        }
        return completeItems;
    }

    private PageRequestDto itemParse(String itemUrl) {
        PageRequestDto itemRequestDto = new PageRequestDto();
        try {
            Document doc = JsoupConnection.createConnection(itemUrl);
            if (Objects.nonNull(doc)) {
                String id = doc.getElementsByClass(HtmlClassNames.ID).size() != 0
                        ? doc.getElementsByClass(ID).text() : "";

                itemRequestDto.setViews(getViews(ParserUtils.mapToNumbers(id).toString()));

                Optional.ofNullable(doc.getElementsByTag(TITLE_TAG).first())
                        .ifPresent(e -> itemRequestDto.setTitle(e.text()));

                Optional.of(doc.getElementsByClass(PRICE))
                        .ifPresent(e -> itemRequestDto.setPrice(e.text()));

                Optional.of(doc.getElementsByClass(DATE))
                        .ifPresent(e -> itemRequestDto.setDate(e.text()));

                Optional.of(doc.getElementsByClass(OBJAST))
                        .ifPresent(e -> itemRequestDto.setOblast(e.text()));

                Optional.ofNullable(doc.getElementsByClass(START_OF_WORK).first())
                        .ifPresent(e -> itemRequestDto.setStartOfWork(e.text()));

                itemRequestDto.setSection(doc.getElementsByClass(SECTION).size() != 0
                        ? doc.getElementsByClass(SECTION).get(1).text() : "");

                Optional.ofNullable(doc.getElementsByClass(DATE_OF_PUBLICATION).first())
                        .ifPresent(e -> itemRequestDto.setDateOfPublication(e.text()));

                itemRequestDto.setTitleForTable(doc.getElementsByClass(TITLE_FOR_TABLE).size() != 0
                        ? doc.getElementsByClass(TITLE_FOR_TABLE).get(1).text() : "");

                Optional.ofNullable(doc.getElementsByClass(INDIVIDUAL).first())
                        .ifPresent(e -> itemRequestDto.setIndividual(e.text()));

                handleListData(doc.getElementsByClass(STATE), itemRequestDto);
                Elements deliveryElement = doc.getElementsByClass(DELIVERY);
                itemRequestDto.setOlxDelivery(deliveryElement.size() != 0);
            }
        } catch (Exception e) {
            log.warn("Can't get item url: " + itemUrl, e);
            return itemRequestDto;
        }
        return itemRequestDto;
    }

    private void handleListData(Elements elements, PageRequestDto pageRequestDto) {
        elements.forEach(element -> {
            if (element.text().contains(STATE)) {
                pageRequestDto.setState(element.text());
            }
        });
    }

    private String getViews(String advertisementId) {
        String wUrl = String.format(OLX_PAGE_VIEWS_URL, advertisementId);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(olxToken);
        httpHeaders.setConnection(KEEP_ALIVE);
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> request = new HttpEntity<>("", httpHeaders);
        String response;
        try {
            response = restTemplate.postForObject(wUrl, request, String.class);
        } catch (Exception e) {
            log.warn("Can't get page views for url: " + wUrl, e);
            return DEFAULT_PAGE_VIEWS;
        }
        return response;
    }
}
