package com.company.pontoworker.dto;

import jakarta.validation.constraints.NotNull;

public abstract class ClockRegistryBaseDTO {

    private Long id;

    @NotNull(message = "O campo userId é obrigatório")
    private Long userId;

    public ClockRegistryBaseDTO() {
    }

    public ClockRegistryBaseDTO(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public ClockRegistryBaseDTO(Long userId) {
        this.userId = userId;
    }

    public abstract ClockRegistryBaseDTO comId(Long id);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
