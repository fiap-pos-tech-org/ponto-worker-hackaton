package br.com.fiap.hackaton.clockregistryworker.mapper;

import br.com.fiap.hackaton.clockregistryworker.dto.UserDTO;
import br.com.fiap.hackaton.clockregistryworker.entity.ClockRegistry;
import br.com.fiap.hackaton.clockregistryworker.entity.User;
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
