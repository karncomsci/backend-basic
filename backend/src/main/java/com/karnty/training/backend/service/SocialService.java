package com.karnty.training.backend.service;

import com.karnty.training.backend.entity.Social;
import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.repository.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialService {

    private final SocialRepository socialRepository;

    public SocialService(SocialRepository socialRepository) {
        this.socialRepository = socialRepository;
    }

    public Optional<Social> findByUser(User user) {
        return socialRepository.findByUser(user);
    }
    public Social create(User user,String facebook,String line,String instagram,String tiktok){
        //TODO: validate

        //create
        Social entity = new Social();
        entity.setUser(user);
        entity.setFacebook(facebook);
        entity.setLine(line);
        entity.setInstagram(instagram);
        entity.setTiktok(tiktok);
        return socialRepository.save(entity);
    }
}
