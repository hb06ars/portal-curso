package com.sistema.sistema.domain.service;

import com.sistema.sistema.domain.entity.AlunoEntity;
import com.sistema.sistema.infra.repository.AlunoRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {

    private final AlunoRepository repository;

    public AlunoService(AlunoRepository repository) {
        this.repository = repository;
    }

    public List<AlunoEntity> findAll() throws UsernameNotFoundException {
        return repository.findAll();
    }

    public AlunoEntity save(AlunoEntity alunoEntity) throws UsernameNotFoundException {
        return repository.save(alunoEntity);
    }
}
