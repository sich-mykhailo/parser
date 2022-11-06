package com.parser.parser.notifications;

public interface EmailService {

    void sendSimpleEmail(String toAddress, String subject, String message);
}
