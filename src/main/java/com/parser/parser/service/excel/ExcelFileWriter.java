package com.parser.parser.service.excel;

import com.parser.parser.dto.Page;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;

public interface ExcelFileWriter {
    FileOutputStream getFile();

    void writeToSheet(FileOutputStream fileOut,
                      Page page, Sheet sheet, Workbook workbook);
}