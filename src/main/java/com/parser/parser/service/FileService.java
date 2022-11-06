package com.parser.parser.service;

import com.parser.parser.entity.Page;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface FileService {
    void saveToFile(Page page, Sheet sheet, Workbook workbook);
}