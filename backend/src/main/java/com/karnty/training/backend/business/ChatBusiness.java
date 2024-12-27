package com.karnty.training.backend.business;

import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.ChatException;
import com.karnty.training.backend.model.request.ChatMessage;
import com.karnty.training.backend.model.request.ChatMessageRequest;
import com.karnty.training.backend.util.SecurityUtil;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ChatBusiness {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatBusiness(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void post(ChatMessageRequest request) throws BaseException {

        final String destination = "/topic/chat";

        Optional<String> opt =  SecurityUtil.getCurrentUserId();

        if(opt.isEmpty()){
            throw ChatException.accessDenied();
        }

        //TODO: validate message

        ChatMessage payload = new ChatMessage();
        payload.setFrom(opt.get());
        payload.setMessage(request.getMessage());

        simpMessagingTemplate.convertAndSend(destination,payload);
    }
}
