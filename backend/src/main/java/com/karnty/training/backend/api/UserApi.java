package com.karnty.training.backend.api;

import com.karnty.training.backend.business.UserBusiness;
import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.UserException;
import com.karnty.training.backend.model.TestResponse;
import com.karnty.training.backend.model.request.MActivateRequest;
import com.karnty.training.backend.model.request.MLoginRequest;
import com.karnty.training.backend.model.request.MRegisterRequest;
import com.karnty.training.backend.model.request.MResendActivationEmailRequest;
import com.karnty.training.backend.model.response.MActivateResponse;
import com.karnty.training.backend.model.response.MLoginResponse;
import com.karnty.training.backend.model.response.MRegisterResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserApi {

    private final UserBusiness business;

    public UserApi(UserBusiness business) {
        this.business = business;
    }

    @GetMapping
    public TestResponse test() {
        TestResponse res = new TestResponse();
        res.setName("John");
        res.setFood("Pizza");
        return res;
    }

    @PostMapping
    @RequestMapping("/register")
    public ResponseEntity<MRegisterResponse> register(@RequestBody MRegisterRequest request) throws BaseException {
        MRegisterResponse response = business.register(request);
        return ResponseEntity.ok(response);

    }
    @GetMapping("/refresh-token")
    public ResponseEntity<String> refreshToken() throws BaseException {
        String response = business.refreshToken();
        return ResponseEntity.ok(response);
    }
    @PostMapping("/upload-profile")
    public ResponseEntity<String> uploadProfilePicture(@RequestPart MultipartFile file) throws BaseException {
        String response = business.uploadProfilePicture(file);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<MLoginResponse> login(@RequestBody MLoginRequest request) throws BaseException {
        MLoginResponse response = business.login(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/activate")
    public ResponseEntity<MActivateResponse> activate(@RequestBody MActivateRequest request) throws BaseException{
        MActivateResponse response = business.activate(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/resend-activation-email")
    public ResponseEntity<Void> resendActivationEmail(@RequestBody MResendActivationEmailRequest request) throws BaseException {
        business.resendActivationEmail(request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
