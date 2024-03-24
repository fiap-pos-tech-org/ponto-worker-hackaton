package br.com.fiap.hackaton.clockregistryworker.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class ClockRegistryDTO extends ClockRegistryBaseDTO {

    @NotNull(message = "O campo timeClockId é obrigatório")
    private Long timeClockId;

    private LocalDateTime time;

    public ClockRegistryDTO() {
    }

    public ClockRegistryDTO(Long id, Long userId, Long timeClockId, LocalDateTime time) {
        super(id, userId);
        this.timeClockId = timeClockId;
        this.time = time;
    }

    @Override
    public ClockRegistryDTO comId(Long id) {
        return new ClockRegistryDTO(id, getUserId(), timeClockId, time);
    }

    public Long getTimeClockId() {
        return timeClockId;
    }

    public void setTimeClockId(Long timeClockId) {
        this.timeClockId = timeClockId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
