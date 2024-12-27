package com.karnty.training.backend.business;

import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.FileException;
import com.karnty.training.backend.exception.UserException;
import com.karnty.training.backend.mapper.UserMapper;
import com.karnty.training.backend.model.request.MLoginRequest;
import com.karnty.training.backend.model.request.MRegisterRequest;
import com.karnty.training.backend.model.response.MLoginResponse;
import com.karnty.training.backend.model.response.MRegisterResponse;
import com.karnty.training.backend.service.TokenService;
import com.karnty.training.backend.service.UserService;
import com.karnty.training.backend.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserBusiness {

    private final UserService userService;
    private final UserMapper userMapper;
    private final TokenService tokenService;

    public UserBusiness(UserService userService, UserMapper userMapper, TokenService tokenService) {
        this.userService = userService;
        this.userMapper = userMapper;
        this.tokenService = tokenService;
    }
    public MLoginResponse login(MLoginRequest request) throws BaseException {
        //validate request

        //verify user database
       Optional<User> opt =  userService.findByEmail(request.getEmail());
       if(opt.isEmpty()){
            throw UserException.loginFailEmailNotFound();
       }
       User user = opt.get();

       if(!userService.matchPassword(request.getPassword(),user.getPassword())){
            throw UserException.loginFailPasswordIncorrect();
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
        User user = userService.createUser(request.getEmail(),request.getPassword(),request.getName());
        //mapper
        return  userMapper.toRegisterResponse(user);
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
}
