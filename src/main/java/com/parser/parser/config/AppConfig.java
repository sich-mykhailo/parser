package com.parser.parser.config;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    //TODO change it
    @PostConstruct
    public void createSheet() {
        Workbook workbook = new XSSFWorkbook();
        workbook.createSheet("newList");
    }
}
