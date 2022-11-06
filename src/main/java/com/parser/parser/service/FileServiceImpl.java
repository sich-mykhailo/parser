package com.parser.parser.service;

import com.parser.parser.entity.Page;
import com.parser.parser.service.excel.ExcelFileWriter;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final ExcelFileWriter excelFileWriter;

    @Override
    public void saveToFile(Page page, Sheet sheet, Workbook workbook) {
            excelFileWriter.writeToSheet(excelFileWriter.getFile(), page, sheet, workbook);
    }
}