package com.sistema.sistema.infra.repository;


import com.sistema.sistema.domain.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<CursoEntity, Long> {
}
