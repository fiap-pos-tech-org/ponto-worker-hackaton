package br.com.fiap.hackaton.clockregistryworker.mapper;

import br.com.fiap.hackaton.clockregistryworker.dto.ClockRegistryDTO;
import br.com.fiap.hackaton.clockregistryworker.entity.ClockRegistry;
import br.com.fiap.hackaton.clockregistryworker.entity.User;
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
