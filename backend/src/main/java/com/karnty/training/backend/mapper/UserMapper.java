package com.karnty.training.backend.mapper;

import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.model.response.MRegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    MRegisterResponse toRegisterResponse(User user);
}
