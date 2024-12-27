package com.karnty.training.backend.exception;

public class UserException extends BaseException {

    public UserException(String code) {
        super("user."+code);
    }
    public static UserException requestNull(){
        return new UserException("register.request.null");
    }
    public static UserException emailNull(){
        return new UserException("register.email.null");
    }
    //create
    public static UserException createEmailNull(){
        return new UserException("create.email.null");
    }
    public static UserException createEmailDuplicate(){
        return new UserException("create.email.duplicate");
    }
    public static UserException createPasswordNull(){
        return new UserException("create.password.null");
    }
    public static UserException createNameNull(){
        return new UserException("create.name.null");
    }
    public static UserException loginFailEmailNotFound(){
        return new UserException("login.fail");
    }
    public static UserException loginFailPasswordIncorrect(){
        return new UserException("login.fail");
    }
    public static UserException notFound(){
        return new UserException("user.not.found");
    }
    public static UserException unAuthorized(){
        return new UserException("unauthorized");
    }


}
