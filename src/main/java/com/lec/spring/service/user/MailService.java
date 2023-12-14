package com.lec.spring.service.user;

import com.lec.spring.domain.user.Email;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class MailService {
    @Autowired
    MailHandler mailHandler;


    private static String FROM_ADDRESS = "UNDEFINED";

    public void mailSend(Email email) throws MessagingException {
        mailHandler.setFrom(FROM_ADDRESS);
        mailHandler.setTo(email.getAddress());
        mailHandler.setSubject(email.getTitle());
        mailHandler.setText(email.getMessage(), true);
        mailHandler.send();
    }
}
