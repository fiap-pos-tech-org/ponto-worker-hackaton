package com.company.pontoworker.service;

import com.company.pontoworker.dto.ClockRegistryDTO;
import com.company.pontoworker.dto.ClockRegistryReportDTO;
import com.company.pontoworker.entity.ClockRegistry;
import com.company.pontoworker.entity.ReportLog;
import com.company.pontoworker.entity.User;
import com.company.pontoworker.exception.EntityNotFoundException;
import com.company.pontoworker.mapper.ClockRegistryMapper;
import com.company.pontoworker.message.producer.Producer;
import com.company.pontoworker.repository.ClockRegistryRepository;
import com.company.pontoworker.repository.ReportLogRepository;
import com.company.pontoworker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ClockRegistryService {

    private final Logger logger = LogManager.getLogger(ClockRegistryService.class);
    private final ClockRegistryRepository clockRegistryRepository;
    private final UserRepository userRepository;
    private final ReportLogRepository reportLogRepository;
    private final Producer reportNotification;
    private final ClockRegistryMapper clockRegistryMapper;

    public ClockRegistryService(ClockRegistryRepository clockRegistryRepository, UserRepository userRepository,
                                ReportLogRepository reportLogRepository, Producer producer, ClockRegistryMapper clockRegistryMapper) {
        this.clockRegistryRepository = clockRegistryRepository;
        this.userRepository = userRepository;
        this.reportLogRepository = reportLogRepository;
        this.reportNotification = producer;
        this.clockRegistryMapper = clockRegistryMapper;
    }

    public ClockRegistryDTO create(ClockRegistryDTO clockRegistryDTO) {
        Long userId = clockRegistryDTO.getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado.", userId)));

        ClockRegistry registry = clockRegistryMapper.toEntity(clockRegistryDTO, user);
        logger.info(String.format("registro de ponto efetuado com sucesso para o usuário com id %s.", userId));

        return clockRegistryMapper.toDto(clockRegistryRepository.save(registry));
    }

    @Transactional
    public void createAndSendReport(ClockRegistryReportDTO registry) {
        Long userId = registry.getUserId();
        var currentDate = LocalDate.now().withMonth(registry.getMonth());
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        var timePattern = formatter.format(currentDate);
        String noRegistriesErrorMessage = String.format("Não há registro de ponto do usuário %s do mês %s", userId, timePattern);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado.", userId)));

        List<ClockRegistry> clockRegistries = clockRegistryRepository.findByUserIdAndTimeLikeOrderByTime(userId, timePattern)
                .orElseThrow(() -> new EntityNotFoundException(noRegistriesErrorMessage));

        String report = createReport(clockRegistries);
        reportLogRepository.save(new ReportLog(LocalDateTime.now(), user));

        reportNotification.publish(report);

        logger.info(String.format("relatório enviado com sucesso para o usuário com id %s.", userId));
    }

    private String createReport(List<ClockRegistry> clockRegistries) {
//        List<String> report = clockRegistries.stream()
//                .map()

        return "report";
    }
}
