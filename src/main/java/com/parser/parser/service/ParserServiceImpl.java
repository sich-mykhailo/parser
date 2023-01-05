package com.parser.parser.service;

import com.parser.parser.dto.PageRequestDto;
import com.parser.parser.entity.Page;
import com.parser.parser.service.mapper.PageDtoMapper;
import com.parser.parser.utils.Constants;
import com.parser.parser.utils.HtmlNames;
import com.parser.parser.utils.JsoupConnection;
import com.parser.parser.utils.ParserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class ParserServiceImpl implements ParserService {
    private final RestTemplate restTemplate;
    private final PageDtoMapper pageDtoMapper;

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
            if (doc != null) {
                String id = Objects.nonNull(doc.getElementsByClass("css-xtucvg-TextStyled er34gjf0"))
                        ? doc.getElementsByClass("css-xtucvg-TextStyled er34gjf0").text() : "-1";
                itemRequestDto.setViews(getViews(ParserUtils.mapToNumbers(id).toString()));
                itemRequestDto.setTitle(doc.getElementsByTag("title").first().text());
                itemRequestDto.setPrice(doc.getElementsByClass(HtmlNames.PRICE).text());
                itemRequestDto.setDate(doc.getElementsByClass(HtmlNames.DATE).text());
                itemRequestDto.setOblast(doc.getElementsByClass("css-tyi2d1").get(4).text());
                //itemRequestDto.setStartOfWork(Objects.requireNonNull(doc.getElementsByClass(HtmlNames.START_OF_WORK).first().text()));
                itemRequestDto.setSection(doc.getElementsByClass("css-tyi2d1").get(1).text());
                itemRequestDto.setDateOfPublication(Objects.nonNull(doc.getElementsByClass("css-sg1fy9").first()) ?
                        doc.getElementsByClass("css-sg1fy9").first().text() : "-");
              //  itemRequestDto.setTitleForTable(doc.getElementsByClass("css-sg1fy9").get(1).text());
               // itemRequestDto.setIndividual(doc.getElementsByClass("css-xl6fe0-Text eu5v0x0").first().text());
                handleListData(doc.getElementsByClass("css-xl6fe0-Text eu5v0x0"), itemRequestDto);
                itemRequestDto.setIsTop(false);
                Elements deliveryElement = doc.getElementsByClass("css-x30oa2-Text eu5v0x0");
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
            if (element.text().contains("Стан:")) {
                pageRequestDto.setState(element.text());
            }
        });
    }

    private String getViews(String advertisementId) {
        String wUrl = "https://www.olx.ua/api/v1/offers/"
                + advertisementId
                + "/page-views/";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", Constants.OLX_TOKEN);
        httpHeaders.setConnection("keep-alive");
        httpHeaders.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> request = new HttpEntity<>("", httpHeaders);
        String s;
        try {
            s = restTemplate.postForObject(wUrl, request, String.class);
        } catch (Exception e) {
            log.warn("Can't get page-views for url: " + wUrl);
            return "0";
        }
        return s;
    }
}
