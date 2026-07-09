package com.concert.backend.auth.infrastructure;

import com.concert.backend.auth.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

}
