package com.ctu.jobhunter.utils.Mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.ctu.jobhunter.domain.User;
import com.ctu.jobhunter.dto.users.ResponseUserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ResponseUserDTO toResponseUserDTO(User user);

    List<ResponseUserDTO> toListResponseUserDTO(List<User> user);
}
