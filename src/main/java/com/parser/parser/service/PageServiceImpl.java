package com.parser.parser.service;

import com.parser.parser.dto.Page;
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
    FileService writeToFileService;
    Workbook workbook = new XSSFWorkbook();
    Sheet sheet = workbook.createSheet("newList");
    ParserService parserService;

    public List<Page> getAllItemsFromMainUrl(String mainUrl) {
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

    public void parsePage(String mainUrl) {
        List<Page> allPagesFromMainUrl = getAllItemsFromMainUrl(mainUrl);
        List<Page> allPages = parserService.getAllItems(allPagesFromMainUrl);
        createSheetTitle();
        allPages.forEach(page -> writeToFileService.saveToFile(page, sheet, workbook));
    }

    private void createSheetTitle() {
        CellStyle cellStyle = workbook.createCellStyle();
        sheet.setColumnWidth(0, 10000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 2000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 7000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 6000);
        sheet.setColumnWidth(8, 6000);
        sheet.setColumnWidth(9, 6000);

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        cellStyle.setFillPattern(FillPatternType.BIG_SPOTS);
        cellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        row.setHeight((short) 400);
        cell.setCellStyle(cellStyle);
        row.createCell(0).setCellValue("Назва");
        row.createCell(1).setCellValue("Перегляди");
        row.createCell(2).setCellValue("Ціна");
        row.createCell(3).setCellValue("Стан");
        row.createCell(4).setCellValue("Область");
        row.createCell(5).setCellValue("Дата реєстрації на олх");
        row.createCell(6).setCellValue("Олх доставка");
        row.createCell(7).setCellValue("Розділ");
        row.createCell(8).setCellValue("Дата публікації");
        row.createCell(9).setCellValue("Категорія");
        row.createCell(10).setCellValue("Топ");
        row.createCell(11).setCellValue("Лінк");
    }
}
