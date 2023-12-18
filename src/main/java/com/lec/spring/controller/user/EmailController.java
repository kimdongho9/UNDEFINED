package com.lec.spring.controller.user;

import com.lec.spring.domain.user.Email;
import com.lec.spring.service.user.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/verify")
    public void verify(Email email) throws MessagingException {
        mailService.mailSend(email);
    }
}
