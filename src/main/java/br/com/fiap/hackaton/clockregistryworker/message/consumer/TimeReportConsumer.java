package br.com.fiap.hackaton.clockregistryworker.message.consumer;

import br.com.fiap.hackaton.clockregistryworker.dto.ClockRegistryReportDTO;
import br.com.fiap.hackaton.clockregistryworker.exception.EntityNotFoundException;
import br.com.fiap.hackaton.clockregistryworker.service.ClockRegistryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class TimeReportConsumer {

    private final Logger logger = LogManager.getLogger(TimeReportConsumer.class);
    private final ObjectMapper mapper;
    private final ClockRegistryService clockRegistryService;

    public TimeReportConsumer(ObjectMapper mapper, ClockRegistryService clockRegistryService) {
        this.mapper = mapper;
        this.clockRegistryService = clockRegistryService;
    }

    @JmsListener(destination = "${aws.sqs.report-queue-name}")
    public void receiveMessage(@Payload String mensagem) {
        logger.info("mensagem recebida do serviço ponto-api: {}", mensagem);
        try {
            var clockRegistryReport = mapper.readValue(mensagem, ClockRegistryReportDTO.class);
            clockRegistryService.createAndSendReport(clockRegistryReport);
        } catch (JsonProcessingException | EntityNotFoundException e) {
            logger.error("erro no fluxo de consumo da mensagem: {}", e.getMessage());
        }
    }
}
