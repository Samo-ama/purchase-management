package com.example.purchase.management.service.impl;

import com.example.purchase.management.config.EmailProperties;
import com.example.purchase.management.exception.ContentSizeExceededException;
import com.example.purchase.management.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailSenderService implements SenderService {

    private final JavaMailSender mailSender;
    private final EmailProperties emailProperties;

     private static final int Size_Limit = 25 * 1024 * 1024;

   

    @Override
    public void send(String subject, String htmlContent)
            throws ContentSizeExceededException
    {
        if (emailProperties.getFrom() == null) {
            throw new IllegalStateException("sender email address is not configured");
        }

        if (htmlContent.getBytes().length > Size_Limit) {
            throw new ContentSizeExceededException();
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(emailProperties.getFrom());
            helper.setTo(emailProperties.getTo());
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent successfully");
        }
        catch (Exception e) {
            log.error("Failed to send email: ", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }


    
}