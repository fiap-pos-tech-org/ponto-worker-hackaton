package com.company.pontoworker.message.consumer;

import com.company.pontoworker.dto.ClockRegistryReportDTO;
import com.company.pontoworker.message.producer.Producer;
import com.company.pontoworker.service.ClockRegistryService;
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
    private final Producer producer;

    private final ClockRegistryService clockRegistryService;

    public TimeReportConsumer(ObjectMapper mapper, Producer producer, ClockRegistryService clockRegistryService) {
        this.mapper = mapper;
        this.producer = producer;
        this.clockRegistryService = clockRegistryService;
    }

    @JmsListener(destination = "${aws.sqs.report-queue-name}")
    public void receiveMessage(@Payload String mensagem) {
        logger.info("mensagem recebida do serviço ponto-api: {}", mensagem);

        ClockRegistryReportDTO clockRegistryReport;
        try {
            clockRegistryReport = mapper.readValue(mensagem, ClockRegistryReportDTO.class);
        } catch (JsonProcessingException e) {
            logger.error("erro ao serializar mensagem: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        clockRegistryService.createAndSendReport(clockRegistryReport);
    }
}
