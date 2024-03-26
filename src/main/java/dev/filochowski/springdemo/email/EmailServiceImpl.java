package dev.filochowski.springdemo.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailSender {
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String recipient, String text) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true);
            helper.setTo(recipient);
            helper.setSubject("Confirm your email");
            helper.setFrom("contact@filochowski.dev");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email");
            throw new IllegalStateException("Failed to send email");
        }
    }
}
