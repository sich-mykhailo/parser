package com.parser.parser.service.excel;

import com.parser.parser.dto.Page;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

@Component
public class ExcelFileWriterImpl implements ExcelFileWriter {

    public void writeToSheet(Page item, Sheet sheet, Workbook workbook) {
        int count = 0;
            Row row = sheet.createRow(Integer.parseInt(String.valueOf(item.getId())));
            row.createCell(count).setCellValue(item.getTitle());
            Cell views = row.createCell(++count);
            viewColorHandler(workbook, views, item.getViews());
            row.createCell(++count).setCellValue(item.getPrice());
            row.createCell(++count).setCellValue(item.getState());
            row.createCell(++count).setCellValue(item.getOblast());
            row.createCell(++count).setCellValue(item.getStartOfWork());
            row.createCell(++count).setCellValue(item.getOlxDelivery());
            row.createCell(++count).setCellValue(item.getSection());
            row.createCell(++count).setCellValue(item.getDateOfPublication());
            row.createCell(++count).setCellValue(item.getIndividual());
            row.createCell(++count).setCellValue(item.getTop());
            Cell cell = row.createCell(++count);
            cell.setCellFormula("HYPERLINK(\""
                    + item.getUrl()
                    + "\", \""
                    + "url link"
                    + "\")");
    }

    private void viewColorHandler(Workbook workbook, Cell views, String value) {
        views.setCellValue(value);
        if (!value.startsWith("-")) {
            int priceValue = Integer.parseInt(value);
            if (priceValue >= 10000) {
                views.setCellStyle(getGreen(workbook));
            }
        }
    }

    private CellStyle getGreen(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(FillPatternType.SQUARES);
        return style;
    }

    public void createSheetTitle(Workbook workbook, Sheet sheet) {
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