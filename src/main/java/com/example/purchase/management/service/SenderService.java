package com.example.purchase.management.service;

public interface SenderService {
    void send(String subject, String htmlContent);
    //void sendHtmlReport(String subject, String htmlContent);
}
