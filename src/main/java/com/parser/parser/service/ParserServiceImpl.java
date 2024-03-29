package com.parser.parser.service;

import com.parser.parser.dto.Page;
import com.parser.parser.service.mapper.PageMapper;
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
import org.springframework.web.client.HttpClientErrorException;
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
    PageMapper pageMapper;

    @Override
    public List<Page> getAllItems(List<Page> pages) {
        long count = 1L;
        List<Page> completeItems = new ArrayList<>();
        log.info("total size of items: " + pages.size());
        for (Page item : pages) {
            if (Objects.nonNull(item.getUrl())) {
                Page dto = itemParse(item.getUrl());
                if (Objects.nonNull(item.getTitle())) {
                    dto.setTitle(item.getTitle());
                }
                dto.setUrl(item.getUrl());
                dto.setTop(item.getTop());
                dto.setOlxDelivery(item.getOlxDelivery());
                Page completeItem = pageMapper.map(dto);
                completeItem.setId(count++);
                completeItems.add(completeItem);
            }
            if (completeItems.size() % 100 == 0) {
                log.info("completed items: " + completeItems.size());
            }
        }
        return completeItems;
    }

    private Page itemParse(String itemUrl) {
        Page itemRequestDto = new Page();
        try {
            Document doc = JsoupConnection.createConnection(itemUrl);
            if (Objects.nonNull(doc)) {
                String id = !doc.getElementsByClass(ID).isEmpty()
                        ? doc.getElementsByClass(ID).text() : "";

                itemRequestDto.setViews(getViews(ParserUtils.mapToNumbers(id).toString()));

                Optional.ofNullable(doc.getElementsByTag(TITLE_TAG).first())
                        .ifPresent(e -> itemRequestDto.setTitle(e.text()));

                Optional.of(doc.getElementsByClass(PRICE))
                        .ifPresent(e -> itemRequestDto.setPrice(e.text()));

                Optional.of(doc.getElementsByClass(DATE))
                        .ifPresent(e -> itemRequestDto.setRegistrationDate(e.text()));

                Optional.of(doc.getElementsByClass(DISTRICT))
                        .ifPresent(e -> itemRequestDto.setDistrict(e.text()));

                Optional.ofNullable(doc.getElementsByClass(START_OF_WORK).first())
                        .ifPresent(e -> itemRequestDto.setStartOfWork(e.text()));

                itemRequestDto.setSection(!doc.getElementsByClass(SECTION).isEmpty()
                        ? doc.getElementsByClass(SECTION).get(1).text() : "");

                Optional.ofNullable(doc.getElementsByClass(DATE_OF_PUBLICATION).first())
                        .ifPresent(e -> itemRequestDto.setDateOfPublication(e.text()));

                Optional.ofNullable(doc.getElementsByClass(INDIVIDUAL).first())
                        .ifPresent(e -> itemRequestDto.setIndividual(e.text()));

                handleListData(doc.getElementsByClass(HtmlClassNames.STATE), itemRequestDto);
                Elements deliveryElement = doc.getElementsByClass(DELIVERY);
                itemRequestDto.setOlxDelivery(!deliveryElement.isEmpty() ? "Є" : "НЕМА");
            }
        } catch (Exception e) {
            log.warn("Can't get item url: " + itemUrl, e);
        }
        return itemRequestDto;
    }

    private void handleListData(Elements elements, Page dto) {
        elements.forEach(element -> {
            if (element.text().contains(STATE)) {
                dto.setState(element.text());
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
        } catch (HttpClientErrorException e) {
            log.warn("Can't get page views for url: "
                    + wUrl
                    + " OLX token has been expired");

            return DEFAULT_PAGE_VIEWS;
        }
        return response;
    }
}
