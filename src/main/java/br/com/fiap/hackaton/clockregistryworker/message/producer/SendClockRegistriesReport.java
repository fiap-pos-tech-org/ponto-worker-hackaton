package br.com.fiap.hackaton.clockregistryworker.message.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;

@Component
public class SendClockRegistriesReport implements ReportNotification {

    private final Logger logger = LogManager.getLogger(SendClockRegistriesReport.class);

    @Value("${aws.sns.report-notification-topic-arn}")
    private String topicArn;
    private final SnsClient snsClient;
    private final ObjectMapper mapper;

    public SendClockRegistriesReport(SnsClient snsClient, ObjectMapper mapper) {
        this.snsClient = snsClient;
        this.mapper = mapper;
    }

    @Override
    public void publish(String report) {
        PublishRequest request = PublishRequest.builder()
                .message(report)
                .topicArn(topicArn)
                .build();

        PublishResponse response = snsClient.publish(request);
        logger.info("mensagem com id {} publicada com sucesso", response.messageId());
    }
}
