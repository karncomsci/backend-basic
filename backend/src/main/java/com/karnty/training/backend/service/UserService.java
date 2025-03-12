package com.karnty.training.backend.service;

import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.exception.UserException;
import com.karnty.training.backend.repository.UserRepository;
import com.karnty.training.backend.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }
    public Optional<User> findByToken(String token){
        return userRepository.findByToken(token);
    }
    public User updateName(String id,String name) throws BaseException {
       Optional<User> opt = userRepository.findById(id);

       if(opt.isEmpty()){
              throw UserException.notFound();
       }
       User user = opt.get();
       user.setName(name);

       return userRepository.save(user);
    }
    public User update(User user){
        return userRepository.save(user);
    }
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
    public void deleteById(String id){
        userRepository.deleteById(id);
    }
    public boolean matchPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword,encodedPassword);
    }
    public User createUser(String email, String password, String name, String token,Date tokenExpireDate) throws BaseException {
        //validate
        if(Objects.isNull(email)){
            throw UserException.createEmailNull();
        }
        if(Objects.isNull(password)){
            throw UserException.createPasswordNull();
        }
        if(Objects.isNull(name)){
            throw UserException.createNameNull();
        }
        //verify
        if(userRepository.existsByEmail(email)){
            throw UserException.createEmailDuplicate();
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setToken(token);
        user.setTokenExpire(tokenExpireDate);

        return userRepository.save(user);
    }

}
