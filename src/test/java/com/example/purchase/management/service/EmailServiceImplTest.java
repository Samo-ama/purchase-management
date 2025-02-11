package com.example.purchase.management.service;

import com.example.purchase.management.config.EmailProperties;
import com.example.purchase.management.service.impl.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailProperties emailProperties;

    private MimeMessage mimeMsg;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mimeMsg = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMsg);
    }

    @Test
    public void sendHtmlEmail_WithValidProperties_ShouldSendReport() throws MessagingException {
        // Arrange
        String subject = "report";
        String htmlContent = "<h1>report data</h1>";
        String mailAddress = "baylasan@test.me";

        // Mock email properties
        when(emailProperties.getFrom()).thenReturn(mailAddress);
        when(emailProperties.getTo()).thenReturn(mailAddress);

        // Act
        emailService.sendHtmlEmail(subject, htmlContent);

        // Assert
        verify(mailSender, times(1)).send(mimeMsg);
    }

    @Test
    public void sendHtmlEmail_WithInvalidProperties_ShouldThrowException() {
        // Arrange
        String subject = "report";
        String htmlContent = "<h1>report data</h1>";
        String mailAddress = "baylasan@test.me";

        when(emailProperties.getFrom()).thenReturn(null);
        when(emailProperties.getTo()).thenReturn(mailAddress);

        // Act & Assert
        var exp = assertThrows(
                RuntimeException.class,
                () -> emailService.sendHtmlEmail(subject, htmlContent)
        );

        assertEquals("Failed to send email", exp.getMessage());
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

}
