package com.sistema.sistema.infra.repository;


import com.sistema.sistema.domain.entity.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {
    Optional<AlunoEntity> findByUsername(String email);
}
