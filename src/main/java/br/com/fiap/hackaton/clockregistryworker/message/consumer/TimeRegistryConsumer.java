package br.com.fiap.hackaton.clockregistryworker.message.consumer;

import br.com.fiap.hackaton.clockregistryworker.dto.ClockRegistryDTO;
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
public class TimeRegistryConsumer {

    private final Logger logger = LogManager.getLogger(TimeRegistryConsumer.class);
    private final ObjectMapper mapper;
    private final ClockRegistryService clockRegistryService;

    public TimeRegistryConsumer(ObjectMapper mapper, ClockRegistryService clockRegistryService) {
        this.mapper = mapper;
        this.clockRegistryService = clockRegistryService;
    }

    @JmsListener(destination = "${aws.sqs.registry-queue-name}")
    public void receiveMessage(@Payload String mensagem) {
        logger.info("mensagem recebida do serviço ponto-api: {}", mensagem);
        try {
            var clockRegistry = mapper.readValue(mensagem, ClockRegistryDTO.class);
            clockRegistryService.create(clockRegistry);
        } catch (JsonProcessingException | EntityNotFoundException e) {
            logger.error("erro no fluxo de consumo da mensagem: {}", e.getMessage());
        }
    }
}
