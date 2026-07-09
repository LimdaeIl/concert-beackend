package com.concert.backend.auth.infrastructure.jpa;

import com.concert.backend.auth.domain.Credential;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByMemberId(Long id);
}
