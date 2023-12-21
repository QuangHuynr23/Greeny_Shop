package com.webnongsan.greenshop.service.impl;

import com.webnongsan.greenshop.model.dto.MailInfo;
import com.webnongsan.greenshop.service.SendEmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class SendEmailService implements SendEmailServiceImpl {
    @Autowired
    JavaMailSender mailSender;
    List<MailInfo> list = new ArrayList<>();

    @Override
    public void send(MailInfo mail) throws MessagingException {
        // Tạo message
        MimeMessage message = mailSender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
        helper.setFrom(mail.getFrom());
        helper.setSubject(mail.getSubject());
        helper.setTo(mail.getTo());
        helper.setText(mail.getBody(), true);
        helper.setReplyTo(mail.getFrom());
        if(mail.getAttachments() != null){
            FileSystemResource file = new FileSystemResource(new File(mail.getAttachments()));
            helper.addAttachment(mail.getAttachments(), file);
        }
        // Gửi message đến SMTP server
        mailSender.send(message);
    }
    @Override
    // Gọi sau mỗi 5s
    @Scheduled(fixedDelay = 5000)
    public void run() {
        while (!list.isEmpty()){
            MailInfo mailInfo =list.remove(0);
            try {
                this.send(mailInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void queue(MailInfo mail) {
        list.add(mail);
    }

    @Override
    public void queue(String to, String subject, String body) {
        queue(new MailInfo(to, subject, body));
    }


}
