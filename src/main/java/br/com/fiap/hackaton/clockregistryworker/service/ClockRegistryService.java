package br.com.fiap.hackaton.clockregistryworker.service;

import br.com.fiap.hackaton.clockregistryworker.dto.ClockRegistryDTO;
import br.com.fiap.hackaton.clockregistryworker.dto.ClockRegistryReportDTO;
import br.com.fiap.hackaton.clockregistryworker.entity.ClockRegistry;
import br.com.fiap.hackaton.clockregistryworker.entity.ReportLog;
import br.com.fiap.hackaton.clockregistryworker.entity.User;
import br.com.fiap.hackaton.clockregistryworker.exception.EntityNotFoundException;
import br.com.fiap.hackaton.clockregistryworker.mapper.ClockRegistryMapper;
import br.com.fiap.hackaton.clockregistryworker.message.producer.ReportNotification;
import br.com.fiap.hackaton.clockregistryworker.repository.ClockRegistryRepository;
import br.com.fiap.hackaton.clockregistryworker.repository.ReportLogRepository;
import br.com.fiap.hackaton.clockregistryworker.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ClockRegistryService {

    private final Logger logger = LogManager.getLogger(ClockRegistryService.class);
    private final ClockRegistryRepository clockRegistryRepository;
    private final UserRepository userRepository;
    private final ReportLogRepository reportLogRepository;
    private final ReportNotification reportNotification;
    private final ClockRegistryMapper clockRegistryMapper;

    public ClockRegistryService(ClockRegistryRepository clockRegistryRepository, UserRepository userRepository,
                                ReportLogRepository reportLogRepository, ReportNotification producer, ClockRegistryMapper clockRegistryMapper) {
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
        var timeQuery = registry.getYearMonth();
        String noRegistriesErrorMessage = String.format("Não há registro de ponto para o usuário %s no mês %s", userId, timeQuery);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Usuário com id %s não encontrado.", userId)));

        List<ClockRegistry> clockRegistries = clockRegistryRepository.findByUserIdAndTimeLikeOrderByTime(userId, timeQuery);
        if (Objects.isNull(clockRegistries) || clockRegistries.isEmpty())
            throw new EntityNotFoundException(noRegistriesErrorMessage);

        List<List<ClockRegistry>> clockRegistriesList = getClockRegistries(clockRegistries);
        String totalHoursWorked = getTotalHoursWorked(clockRegistriesList);
        ReportLog reportLog = new ReportLog(LocalDateTime.now(), user);

        reportLogRepository.save(reportLog);
        reportNotification.publish(createReport(clockRegistries, totalHoursWorked));

        logger.info(String.format("relatório enviado com sucesso para o usuário com id %s.", userId));
    }

    private List<List<ClockRegistry>> getClockRegistries(List<ClockRegistry> clockRegistries) {
        return clockRegistries.stream()
                .collect(Collectors.groupingBy(clock -> clock.getTime().toLocalDate()))
                .entrySet()
                .stream()
                .map(entry -> entry.getValue().stream()
                        .map(clock -> new ClockRegistry(clock.getTimeClockId(), clock.getTime()))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private String getTotalHoursWorked(List<List<ClockRegistry>> clockRegistries) {
        Duration totalHoursWorked = Duration.ZERO;
        for (List<ClockRegistry> entry : clockRegistries) {
            totalHoursWorked = totalHoursWorked.plus(calculateTotalHoursWorked(entry));
        }
        return formatTotalHoursWorked(totalHoursWorked);
    }

    private Duration calculateTotalHoursWorked(List<ClockRegistry> clockRegistries) {
        var totalHoursWorked = Duration.ZERO;
        for (int i = 0; i < clockRegistries.size() - 1; i++) {
            var initialDate = clockRegistries.get(i).getTime();
            var endDate = clockRegistries.get(i + 1).getTime();
            var difference = Duration.between(initialDate, endDate);
            totalHoursWorked = totalHoursWorked.plus(difference);
        }
        return totalHoursWorked;
    }

    private String formatTotalHoursWorked(Duration totalHoursWorked) {
        return String.format("%02d:%02d:%02d",
                totalHoursWorked.toHours(),
                totalHoursWorked.toMinutesPart(),
                totalHoursWorked.toSecondsPart()
        );
    }

    private String createReport(List<ClockRegistry> clockRegistries, String totalHoursWorked) {
        List<String> clockRegistriesAsString = getStringClockRegistries(clockRegistries);

        return String.format("Lista de registros:%n%s %n%nTotal Horas Trabalhadas: %s",
                String.join("\n", clockRegistriesAsString), totalHoursWorked);
    }

    private List<String> getStringClockRegistries(List<ClockRegistry> clockRegistries) {
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return clockRegistries.stream()
                .map(ClockRegistry::getTime)
                .map(formatter::format)
                .toList();
    }
}
