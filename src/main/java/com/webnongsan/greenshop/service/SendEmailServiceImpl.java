package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.MailInfo;

import javax.mail.MessagingException;
import java.io.IOException;

public interface SendEmailServiceImpl {
    void run();

    void queue(String to, String subject, String body);

    void queue(MailInfo mail);

    void send(MailInfo mail) throws MessagingException, IOException;
}
