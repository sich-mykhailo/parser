package com.parser.parser.service;

import com.parser.parser.dto.Page;
import com.parser.parser.service.excel.ExcelFileWriter;
import com.parser.parser.utils.HtmlClassNames;
import com.parser.parser.utils.JsoupConnection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.parser.parser.utils.HtmlClassNames.*;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PageServiceImpl implements PageService {

    static int FIRST_PAGE_NUMBER = 0;
    static String ATTRIBUTE_CLASS = "class";
    static String ATTRIBUTE_ALT = "alt";
    static String PAGE_CONNECTOR = "?page=";
    static String TOP = "ТОП";
    static String NOT_TOP = "НЕ ТОП";
    @Value("${page.max}")
    @NonFinal
    int maxNumberOfPages;
    ExcelFileWriter excelFileWriter;
    ParserService parserService;

    protected List<Page> getAllItemsFromMainUrl(String mainUrl) {
        int olxPage = FIRST_PAGE_NUMBER;
        Set<Page> items = new HashSet<>();
        while (olxPage < maxNumberOfPages) {
            olxPage++;
            String completePage = mainUrl + PAGE_CONNECTOR + olxPage;
            Document doc = JsoupConnection.createConnection(completePage);
            log.info("page number:" + olxPage);
            if (Objects.nonNull(doc) && olxPage < 2) {
                Elements elements = doc
                        .getElementsByAttributeValue(ATTRIBUTE_CLASS, HtmlClassNames.FIST_PAGE_CLASS_NAME);
                for (Element element : elements) {
                    String localUrl = element.attributes().get(HtmlClassNames.URL_ATTRIBUTE_NAME);
                    String title = Objects.requireNonNull(element.children().first()).attributes().get(ATTRIBUTE_ALT);
                    Page itemRequestDto = new Page();
                    itemRequestDto.setUrl(localUrl);
                    itemRequestDto.setTitle(title);
                    itemRequestDto.setOlxDelivery(element.getElementsByClass(OLX_DELIVERY).size() != 0 ? "1" : "0");
                    itemRequestDto.setTop(element.getElementsByClass(TOP_CLASS).size() != 0 ? TOP : NOT_TOP);
                    items.add(itemRequestDto);
                }
            } else if (Objects.nonNull(doc)) {
                Elements elements = doc.getElementsByClass(HtmlClassNames.CLASS_NAME);
                for (Element element : elements) {
                    String pageUrl = HtmlClassNames.OLX_BASE
                            + element.attributes().get(HtmlClassNames.URL_ATTRIBUTE_NAME);
                    Page pageRequestDto = new Page();
                    pageRequestDto.setUrl(pageUrl);
                    pageRequestDto.setOlxDelivery(element
                            .getElementsByClass(OLX_DELIVERY_FOR_SECOND_PAGE).size() != 0 ? "1" : "0");
                    pageRequestDto.setTop(element
                            .getElementsByClass(TOP_CLASS_FOR_SECOND_PAGE).text().equals(TOP) ? TOP : NOT_TOP);
                    items.add(pageRequestDto);
                }
            }
        }
        return new ArrayList<>(items);
    }

    public ByteArrayInputStream parsePage(String mainUrl) {
        List<Page> allPagesFromMainUrl = getAllItemsFromMainUrl(mainUrl);
        List<Page> allPages = parserService.getAllItems(allPagesFromMainUrl);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("newList");
            excelFileWriter.createSheetTitle(workbook, sheet);
            allPages.forEach(page -> excelFileWriter.writeToSheet(page, sheet, workbook));
            workbook.write(outputStream);
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to sheet");
        }
    }
}
