package com.parser.parser.amazon.sqs.service;

import com.google.api.services.drive.model.File;
import com.parser.parser.google.drive.GoogleDriveService;
import com.parser.parser.service.PageService;
import com.parser.parser.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static com.parser.parser.utils.Constants.*;

@Service
@RequiredArgsConstructor
public class SqsServiceImpl implements SqsService {
    private final PageService pageService;
    private final GoogleDriveService googleDriveService;
    private final RestTemplate restTemplate;
    @Value("${cloud.aws.end-point.uri}")
    private String endPoint;
    private final QueueMessagingTemplate sqs;

    public void putMessageInQueue(String payload, String userEmail, String userId, String input) {
        Message<String> msg = MessageBuilder.withPayload(payload)
                .setHeader(SENDER_HEADER, userId)
                .setHeader(USER_EMAIL_HEADER, userEmail)
                .setHeader(USER_INPUT_HEADER, input)
                .build();
        sqs.send(endPoint, msg);
    }

    @SqsListener(value = Constants.QUEUE_NAME, deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void receiveMessage(String message,
                               @Header(SENDER_HEADER) String senderId,
                               @Header(USER_EMAIL_HEADER) String userEmail,
                               @Header(USER_INPUT_HEADER) String userInput,
                               @Header(BOT_BASE_URL) String botUrl,
                               Acknowledgment acknowledgment) {
        acknowledgment.acknowledge();
        pageService.parsePage(userInput);
        String googleFileUrl = "Default message";
        File file = googleDriveService.sendFile(userEmail);
        if (file != null) {
            googleFileUrl = googleDriveService.getGoogleFileUrl(file);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> body = new HashMap<>();
        body.put("email", userEmail);
        body.put("fileUrl", googleFileUrl);
        body.put("chatId", senderId);
        body.put("userInput", userInput);
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(botUrl + "/send-queue", requestEntity, String.class);
    }
}