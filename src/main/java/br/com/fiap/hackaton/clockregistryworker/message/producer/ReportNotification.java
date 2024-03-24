package br.com.fiap.hackaton.clockregistryworker.message.producer;

public interface ReportNotification {
    void publish(String report);
}
