package com.ctu.jobhunter.utils.Mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.ctu.jobhunter.domain.Company;
import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.dto.auth.ResponseLoginDTO;
import com.ctu.jobhunter.dto.users.ResponseUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Mapping UserEntity -> ResponseLoginDTO.UserLogin
    ResponseLoginDTO.UserLogin toUserLoginDTO(User user);

    ResponseLoginDTO.UserGetAccount toUserGetAccount(User user);

    ResponseUserDTO.Company toResponseUserCompany(Company company);

    ResponseUserDTO toResponseUserDTO(User user);

    List<ResponseUserDTO> toListResponseUserDTO(List<User> users);
}
