package com.karnty.training.backend.business;

import ch.qos.logback.core.util.StringUtil;
import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.FileException;
import com.karnty.training.backend.exception.UserException;
import com.karnty.training.backend.mapper.UserMapper;
import com.karnty.training.backend.model.request.MActivateRequest;
import com.karnty.training.backend.model.request.MLoginRequest;
import com.karnty.training.backend.model.request.MRegisterRequest;
import com.karnty.training.backend.model.request.MResendActivationEmailRequest;
import com.karnty.training.backend.model.response.MActivateResponse;
import com.karnty.training.backend.model.response.MLoginResponse;
import com.karnty.training.backend.model.response.MRegisterResponse;
import com.karnty.training.backend.service.TokenService;
import com.karnty.training.backend.service.UserService;
import com.karnty.training.backend.util.SecurityUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@Log4j2
public class UserBusiness {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;
    private final EmailBusiness emailBusiness;

    public UserBusiness(UserService userService, UserMapper userMapper, TokenService tokenService, EmailBusiness emailBusiness) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
        this.emailBusiness = emailBusiness;
    }
    public MLoginResponse login(MLoginRequest request) throws BaseException {
        //validate request

        //verify user database
       Optional<User> opt =  userService.findByEmail(request.getEmail());
       if(opt.isEmpty()){
            throw UserException.loginFailEmailNotFound();
       }
       User user = opt.get();
        //verify password
       if(!userService.matchPassword(request.getPassword(),user.getPassword())){
            throw UserException.loginFailPasswordIncorrect();
       }
       //validate activate status
        if(!user.isActivated()){
            throw UserException.loginFailUnactivated();
        }
       MLoginResponse response = new MLoginResponse();
        //TODO: generate JWT
       response.setToken(tokenService.tokenize(user));

       return response;
    }
    public String refreshToken() throws BaseException {
       Optional<String> opt =   SecurityUtil.getCurrentUserId();
         if(opt.isEmpty()) {
             throw UserException.unAuthorized();
         }
        String userId = opt.get();
        Optional<User> optUser = userService.findById(userId);

        if(optUser.isEmpty()){
            throw  UserException.notFound();
        }
        User user = optUser.get();
        return tokenService.tokenize(user);
    }
    public MRegisterResponse register(@RequestBody MRegisterRequest request) throws BaseException {
        String token = SecurityUtil.generateToken();
        User user = userService.createUser(request.getEmail(),request.getPassword(),request.getName(),token, nextXMinute(30));

        sentEmail(user);
        //mapper
        return  userMapper.toRegisterResponse(user);
    }
    public void sentEmail(User user){
        //TODO: generate token
        String token = user.getToken();
        try {
            this.emailBusiness.sendActivatedUserEmail(user.getEmail(),user.getName(),token);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String uploadProfilePicture(MultipartFile file) throws BaseException {
        if(file == null){
            throw FileException.fileNull();
        }
        if(file.getSize() > 1048576 * 2){
            throw FileException.fileMaxSize();
        }
        String contentType = file.getContentType();
        if(contentType == null){
            throw  FileException.fileTypeNotSupported();
        }

        List<String> supportedTypes = Arrays.asList("image/jpeg", "image/png");
        if(!supportedTypes.contains(contentType)){
            throw FileException.fileTypeNotSupported();
        }

        //TODO: upload file File storage (AWS,S3,etc...
        try{
            byte[] bytes = file.getBytes();
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return "";
    }
    public MActivateResponse activate(MActivateRequest request) throws BaseException {
        String token = request.getToken();
        if(StringUtil.isNullOrEmpty(token)){
            throw UserException.activateTokenNull();
        }
        Optional<User> opt = userService.findByToken(token);
        if(opt.isEmpty()){
            throw UserException.activateFail();
        }
        User user = opt.get();
        Date now = new Date();
        Date expireDate = user.getTokenExpire();
        if(now.after(expireDate)){
            throw UserException.activateFail();
        }
        user.setActivated(true);
        boolean result = userService.update(user).isActivated();

        MActivateResponse response = new MActivateResponse();
        response.setSuccess(result);

        return response;
    }
    public void resendActivationEmail(MResendActivationEmailRequest request) throws BaseException {
        String email = request.getEmail();
        if(StringUtil.isNullOrEmpty(email)){
            throw UserException.resendActivationEmailNoEmail();
        }
        Optional<User> opt = userService.findByEmail(email);
        if(opt.isEmpty()){
            throw UserException.resendActivationFail();
        }
        User user = opt.get();
        if(user.isActivated()){
            throw UserException.activateAlready();
        }
        //SET NEW TOKEN
        user.setToken(SecurityUtil.generateToken());
        user.setTokenExpire(nextXMinute(30));
        user = userService.update(user);
        sentEmail(user);
    }
    private Date nextXMinute(int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
}
