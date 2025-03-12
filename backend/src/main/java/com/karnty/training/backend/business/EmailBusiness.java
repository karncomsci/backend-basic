package com.karnty.training.backend.business;

import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.EmailException;
import com.karnty.training.common.model.request.EmailRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class EmailBusiness {

    private final KafkaTemplate<String, EmailRequest> kafkaEmailTemplate;

    public EmailBusiness(KafkaTemplate<String, EmailRequest> kafkaEmailTemplate) {
        this.kafkaEmailTemplate = kafkaEmailTemplate;
    }


    public void sendActivatedUserEmail(String email,String name,String token) throws BaseException {
        try {
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", name);
            variables.put("verificationUrl", "http://localhost:8080/activate/"+token);

            EmailRequest emailRequest = new EmailRequest();
            emailRequest.setTo(email);
            emailRequest.setSubject("Please activate your account");
            emailRequest.setContent(variables);

            kafkaEmailTemplate.send("activation-email", emailRequest);
            //emailService.sendMail(emailRequest);

        } catch (Exception e) {
            throw EmailException.templateNotFound();
        }
    }

    /*private final EmailService emailService;

    public EmailBusiness(EmailService emailService) {
        this.emailService = emailService;
    }
    public void sendActivatedUserEmail(String email,String name,String token) throws BaseException {

        log.info("Token = {}", token);

        try {
            Map<String, Object> variables = new HashMap<>();
            //variables.put("name",name);
            //variables.put("link", "http://localhost:8080/activate/"+token);
            variables.put("name", name);
            variables.put("verificationUrl", "http://localhost:8080/activate/"+token);


            emailService.sendHtmlEmail(email, "Please activate your account", "email-template", variables);

        } catch (Exception e) {
            throw EmailException.templateNotFound();
        }
    } */


}
