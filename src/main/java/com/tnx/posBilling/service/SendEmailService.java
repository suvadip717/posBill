package com.tnx.posBilling.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendEmailService {
    @Autowired
    private JavaMailSender sender;

    public void sendVerificationEmail(String email, String subject, LocalDate exDateTime, String referId) {
        sendEmail(email, subject, "Hi,\n" + //
                "\n" + //
                "We are sorry to let you know that there was an unexpected delay beyond the promised time in resolving your issue. We are committed to solving it at the earliest.\n"
                + //
                "\n" + //
                "The revised resolution time is " + exDateTime + ". We apologize for the inconvenience.\n" + //
                "\n" + //
                "For reference, use this incident number: " + referId + "\n" + //
                "\n" + //
                "Warm Regards,\n" + //
                "\n" + //
                "BBPlus Support");
    }

    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setTo(to);
            mail.setSubject(subject);
            mail.setText(body);
            sender.send(mail);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}