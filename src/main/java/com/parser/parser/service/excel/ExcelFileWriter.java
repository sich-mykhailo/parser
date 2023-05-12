package com.parser.parser.service.excel;

import com.parser.parser.dto.Page;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface ExcelFileWriter {

   void writeToSheet(Page page, Sheet sheet, Workbook workbook);

   void createSheetTitle(Workbook workbook, Sheet sheet);
}