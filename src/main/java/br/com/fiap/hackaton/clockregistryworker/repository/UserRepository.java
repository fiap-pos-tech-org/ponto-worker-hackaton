package br.com.fiap.hackaton.clockregistryworker.repository;

import br.com.fiap.hackaton.clockregistryworker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsernameOrEmail(String username, String email);
}
