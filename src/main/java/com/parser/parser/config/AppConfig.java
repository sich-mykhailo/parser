package com.parser.parser.config;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void createSheet() {
        try (Workbook workbook = new XSSFWorkbook()) {
            workbook.createSheet("newList");
        } catch (IOException e) {
            throw new RuntimeException("Can't create new sheet", e);
        }
    }
}
