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
import org.springframework.mail.MailException;
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

        // Mock
        when(emailProperties.getFrom()).thenReturn(mailAddress);
        when(emailProperties.getTo()).thenReturn(mailAddress);

        // Act
        emailService.sendHtmlEmail(subject, htmlContent);

        // Assert
        verify(mailSender, times(1)).send(mimeMsg);
    }

    @Test
    public void sendHtmlEmail_WithInvalidProperties_ShouldThrowRuntimeException() {
        // Arrange
        String subject = "report";
        String htmlContent = "<h1>report data</h1>";
        String mailAddress = "baylasan@test.me";

        when(emailProperties.getFrom()).thenReturn(null);
        when(emailProperties.getTo()).thenReturn(mailAddress);

        // Act & Assert
        var exp = assertThrows(
                RuntimeException.class,
                () -> emailService.sendHtmlEmail(subject, htmlContent));

        assertEquals("Failed to send email", exp.getMessage());
        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    public void sendHtmlEmail_WhenSendingMessageFails_ShouldThrowRuntimeException() throws Exception {
        // Arrange
        String subject = "Test Subject";
        String htmlContent = "<h1>Test Email</h1>";
        String fromEmail = "test@example.com";
        String toEmail = "recipient@example.com";

        when(emailProperties.getFrom()).thenReturn(fromEmail);
        when(emailProperties.getTo()).thenReturn(toEmail);

        doThrow(new MailException("Failed to send") {})
                .when(mailSender).send(any(MimeMessage.class));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emailService.sendHtmlEmail(subject, htmlContent);
        });

        assertEquals("Failed to send email", exception.getMessage());
    }


}