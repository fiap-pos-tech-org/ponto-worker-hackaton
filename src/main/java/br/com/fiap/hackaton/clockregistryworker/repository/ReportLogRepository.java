package br.com.fiap.hackaton.clockregistryworker.repository;

import br.com.fiap.hackaton.clockregistryworker.entity.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
}
