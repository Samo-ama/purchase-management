package com.example.purchase.management.service;

public interface EmailService {
    void sendHtmlEmail(String to, String subject, String htmlContent);
}
