package com.company.pontoworker.repository;

import com.company.pontoworker.entity.ReportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportLogRepository extends JpaRepository<ReportLog, Long> {
}
