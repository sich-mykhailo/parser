package com.parser.parser.service;

import com.parser.parser.dto.Page;
import com.parser.parser.service.excel.ExcelFileWriter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class FileServiceImpl implements FileService {
    ExcelFileWriter excelFileWriter;

    @Override
    public void saveToFile(Page page, Sheet sheet, Workbook workbook) {
        excelFileWriter.writeToSheet(excelFileWriter.getFile(), page, sheet, workbook);
    }
}