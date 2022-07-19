package com.yaanlabs.healthpredictionapi.service;

import com.yaanlabs.healthpredictionapi.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${application.reset.password.landing.page}")
    String resetPasswordPage;

    @Value("${spring.mail.username}")
    String emailFrom;

    @Async
    public void sendResetPasswordEmail(User user) throws MessagingException {
        MimeMessage forgotPasswordEmail = this.emailSender.createMimeMessage();
        forgotPasswordEmail.setFrom(emailFrom);
        forgotPasswordEmail.setRecipients(Message.RecipientType.TO, user.getEmail());
        forgotPasswordEmail.setSubject("Health Traffic Lights Account Password Reset");

        String messageBody = "Dear " + user.getName() + ", <br/>" +
                "Please click the link below to reset your password of Health Traffic Light account.<br/>" +
                "<a href='" + getResetPasswordLink(user) + "'>Reset Password</a>" +
                "<br/><br/>Best Regards,<br/>" +
                "Health Traffic Light Support Team";
        forgotPasswordEmail.setText(messageBody, "UTF-8", "html");

        emailSender.send(forgotPasswordEmail);
    }

    private String getResetPasswordLink(User user) {
        return UriComponentsBuilder.fromUriString(resetPasswordPage)
                .queryParam("email", user.getEmail())
                .queryParam("token", user.getPassword())
                .build().toUriString();
    }
}
