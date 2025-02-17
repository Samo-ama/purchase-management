package com.example.purchase.management.service;

import com.example.purchase.management.exception.ContentSizeExceededException;

public interface SenderService {
    void send(String subject, String htmlContent) throws ContentSizeExceededException;
    //void sendHtmlReport(String subject, String htmlContent);
}
