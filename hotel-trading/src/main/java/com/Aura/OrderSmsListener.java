package com.Aura;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class OrderSmsListener {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @RabbitListener(queuesToDeclare = @Queue("order.sms.queue"))
    public void processSmsNotification(String message) {
        try {
            String[] parts = message.split("\\|", 2);
            if (parts.length < 2) return;
            String toEmail = parts[0];
            String body = parts[1];

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Aura酒店 - 订单通知");
            helper.setText("您有新的订单：" + body, false);
            mailSender.send(mime);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
