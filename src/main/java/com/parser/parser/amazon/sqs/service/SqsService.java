package com.parser.parser.amazon.sqs.service;


public interface SqsService {

    void putMessageInQueue(String payload, String userId, String userEmail, String userInput); // TODO change to QueueDto
}
