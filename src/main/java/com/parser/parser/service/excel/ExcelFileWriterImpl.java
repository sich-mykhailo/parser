package com.parser.parser.service.excel;

import com.parser.parser.entity.Page;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import java.io.*;

@Component
public class ExcelFileWriterImpl implements ExcelFileWriter {

    public void writeToSheet(FileOutputStream fileOut, Page item,
                             Sheet sheet, Workbook workbook) {
        int count = 0;
        try {
            Row row = sheet.createRow(Integer.parseInt(String.valueOf(item.getId())));
            row.createCell(count).setCellValue(item.getTitleForTable());
            row.createCell(++count).setCellValue(item.getViews());
            row.createCell(++count).setCellValue(item.getPrice());
            row.createCell(++count).setCellValue(item.getState());
            row.createCell(++count).setCellValue(item.getOblast());
            row.createCell(++count).setCellValue(item.getStartOfWork());
            row.createCell(++count).setCellValue(item.getOlxDelivery());
            row.createCell(++count).setCellValue(item.getSection());
            row.createCell(++count).setCellValue(item.getDateOfPublication());
            row.createCell(++count).setCellValue(item.getIndividual());
            row.createCell(++count).setCellValue(item.getIsTop());
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

    public FileOutputStream getFile() {
        try {
            return new FileOutputStream(
                    "src/main/resources/excels/newFile.xlsx"); // change it
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Can't find file ", e);
        }
    }
}