package com.karnty.training.backend.business;

import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.EmailException;
import com.karnty.training.backend.service.EmailService;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailBusiness {

    private final EmailService emailService;

    public EmailBusiness(EmailService emailService) {
        this.emailService = emailService;
    }
    public void sendActivatedUserEmail(String email,String name,String token) throws BaseException {

        try {
            Map<String, Object> variables = new HashMap<>();
            //variables.put("name",name);
            //variables.put("link", "http://localhost:8080/activate/"+token);
            variables.put("name", name);
            variables.put("verificationUrl", "http://localhost:8080/activate/"+token);


            emailService.sendHtmlEmail(email, "ยืนยันอีเมลของคุณ", "email-template", variables);

        } catch (Exception e) {
            throw EmailException.templateNotFound();
        }
    }


}
