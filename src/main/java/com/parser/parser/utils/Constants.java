package com.parser.parser.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String GMAIL_TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String CURRENT_USER_ID = "me";
    public static final String LOCAL_FILE_PATH = "src/main/resources/excels/newFile.xlsx";
    public static final String GOOGLE_FILE_URL = "https://docs.google.com/spreadsheets/d/";
    public static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final int NUMBER_OF_OLX_PAGES = 10;
    public static final String OLX_TOKEN = "Bearer 79845bda81d79999a2eaa82ab497ed7bc02acf47";

    /** AWS */
    public static final String QUEUE_NAME = "report-queue";
    public static final String SENDER_HEADER = "SenderId";
    public static final String USER_EMAIL_HEADER = "UserEmail";
    public static final String USER_INPUT_HEADER = "UserInput";
}