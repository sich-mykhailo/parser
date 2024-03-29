package com.parser.parser.utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
    public static final String GMAIL_TOKEN_URL = "https://oauth2.googleapis.com/token";
    public static final String CURRENT_USER_ID = "me";
    public static final String GOOGLE_FILE_URL = "https://docs.google.com/spreadsheets/d/";

    /** AWS */
    public static final String QUEUE_NAME = "report-queue";
    public static final String SENDER_HEADER = "SenderId";
    public static final String USER_EMAIL_HEADER = "UserEmail";
    public static final String USER_INPUT_HEADER = "UserInput";
    public static final String BOT_BASE_URL = "baseUrl";

    public static final String[] MONTHS_IN_NOMINATIVE_CASE_UA  = {
            "січень", "лютий", "березень", "квітень", "травень", "червень",
            "липень", "серпень", "вересень", "жовтень", "листопад", "грудень"
    };

    public static final String[] MONTHS_IN_GENITIVE_CASE_UA = {
            "січня", "лютого", "березня", "квітня", "травня", "червня",
            "липня", "серпня", "вересня", "жовтня", "листопада", "грудня"
    };
}