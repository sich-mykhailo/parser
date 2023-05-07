package com.parser.parser.service.excel;

import com.parser.parser.dto.Page;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class ExcelFileWriterImpl implements ExcelFileWriter {
    @Value("${file.path}")
    private String filePath;

    public void writeToSheet(FileOutputStream fileOut, Page item,
                             Sheet sheet, Workbook workbook) {
        int count = 0;
        try {
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
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to sheet");
        }
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

    private CellStyle getRed(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(FillPatternType.SQUARES);
        return style;
    }

    private CellStyle getYellow(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SQUARES);
        return style;
    }

    public FileOutputStream getFile() {
        try {
            return new FileOutputStream(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't find file ", e);
        }
    }
}