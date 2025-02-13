package com.example.purchase.management.service.impl;

import com.example.purchase.management.config.EmailProperties;
import com.example.purchase.management.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
     private final EmailProperties emailProperties;
    //private static final String FROM_EMAIL = "test.test13390@outlook.com";
    //private static final String TO_EMAIL = "Ebtisamalaama@gmail.com";

    @Override
    public void sendHtmlEmail(String subject, String htmlContent) {
        try {
            if (emailProperties.getFrom() == null) {
                throw new IllegalStateException("From email address is not configured");
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailProperties.getFrom());
            helper.setTo(emailProperties.getTo());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent successfully");
        } catch (Exception e) {
            log.error("Failed to send email: ", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }


    

    
}