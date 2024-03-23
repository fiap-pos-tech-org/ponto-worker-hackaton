package com.company.pontoworker.mapper;

import com.company.pontoworker.dto.ClockRegistryDTO;
import com.company.pontoworker.entity.ClockRegistry;
import com.company.pontoworker.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ClockRegistryMapper {
    public ClockRegistryDTO toDto(ClockRegistry registry) {
        return new ClockRegistryDTO(
                registry.getId(),
                registry.getUser().getId(),
                registry.getTimeClockId(),
                registry.getTime()
        );
    }

    public ClockRegistry toEntity(ClockRegistryDTO registryDTO, User user) {
        return new ClockRegistry(
                registryDTO.getTimeClockId(),
                registryDTO.getTime(),
                user
        );
    }
}
