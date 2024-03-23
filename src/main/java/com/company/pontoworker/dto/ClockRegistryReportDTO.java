package com.company.pontoworker.dto;

public class ClockRegistryReportDTO extends ClockRegistryBaseDTO {

    private Integer month;

    public ClockRegistryReportDTO() {
    }

    public ClockRegistryReportDTO(Long id, Long userId, Integer month) {
        super(id, userId);
        this.month = month;
    }

    public ClockRegistryReportDTO(Long userId, Integer month) {
        super(userId);
        this.month = month;
    }

    @Override
    public ClockRegistryReportDTO comId(Long id) {
        return new ClockRegistryReportDTO(id, getUserId(), month);
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }
}
