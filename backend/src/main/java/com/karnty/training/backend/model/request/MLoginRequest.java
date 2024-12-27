package com.karnty.training.backend.model.request;

import lombok.Data;

@Data
public class MLoginRequest {
    private String email;
    private String password;
}
