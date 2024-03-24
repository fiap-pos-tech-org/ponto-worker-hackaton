package br.com.fiap.hackaton.clockregistryworker.repository;

import br.com.fiap.hackaton.clockregistryworker.entity.ClockRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClockRegistryRepository extends JpaRepository<ClockRegistry, Long> {

    @Query(value = "SELECT * FROM clock_registry WHERE user_id = :userId AND time LIKE :timePattern% ORDER BY time", nativeQuery = true)
    List<ClockRegistry> findByUserIdAndTimeLikeOrderByTime(Long userId, String timePattern);
}
