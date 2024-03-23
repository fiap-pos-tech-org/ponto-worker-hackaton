package com.company.pontoworker.mapper;

import com.company.pontoworker.dto.UserDTO;
import com.company.pontoworker.entity.ClockRegistry;
import com.company.pontoworker.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {
    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getName()
        );
    }

    public User toEntity(UserDTO userDto, List<ClockRegistry> registries) {
        return new User(
                userDto.getUsername(),
                userDto.getPassword(),
                userDto.getEmail(),
                userDto.getName(),
                registries
        );
    }
}
