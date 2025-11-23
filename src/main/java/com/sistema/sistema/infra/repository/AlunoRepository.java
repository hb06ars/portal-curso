package com.sistema.sistema.infra.repository;


import com.sistema.sistema.domain.entity.AlunoEntity;
import com.sistema.sistema.domain.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<AlunoEntity, Long> {
    Optional<AlunoEntity> findByUsername(String email);
}
