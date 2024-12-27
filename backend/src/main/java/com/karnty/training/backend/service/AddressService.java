package com.karnty.training.backend.service;

import com.karnty.training.backend.entity.Address;
import com.karnty.training.backend.entity.Social;
import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.repository.AddressRepository;
import com.karnty.training.backend.repository.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }
    public List<Address> findByUser(User user) {
        return addressRepository.findByUser(user);
    }

    public Address create(User user,String line1,String line2,String zipcode){
        //TODO: validate

        //create
        Address entity = new Address();
        entity.setUser(user);
        entity.setLine1(line1);
        entity.setLine2(line2);
        entity.setZipCode(zipcode);
        return addressRepository.save(entity);
    }
}
