package br.com.fiap.hackaton.clockregistryworker.dto;

public class ClockRegistryReportDTO extends ClockRegistryBaseDTO {

    private String yearMonth;

    public ClockRegistryReportDTO() {
    }

    public ClockRegistryReportDTO(Long id, Long userId, String yearMonth) {
        super(id, userId);
        this.yearMonth = yearMonth;
    }

    public ClockRegistryReportDTO(Long userId, String yearMonth) {
        super(userId);
        this.yearMonth = yearMonth;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
