package com.sistema.sistema.domain.service;

import com.sistema.sistema.domain.entity.CursoEntity;
import com.sistema.sistema.infra.repository.CursoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public List<CursoEntity> findAll() throws UsernameNotFoundException {
        return repository.findAll();
    }

    public CursoEntity save(CursoEntity cursoEntity) throws UsernameNotFoundException {
        return repository.save(cursoEntity);
    }
}
