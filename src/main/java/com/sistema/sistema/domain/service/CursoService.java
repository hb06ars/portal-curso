package com.sistema.sistema.domain.service;

import com.sistema.sistema.domain.dto.CursoDTO;
import com.sistema.sistema.domain.entity.CursoEntity;
import com.sistema.sistema.infra.repository.CursoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;

@Service
@Slf4j
public class CursoService {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = repository;
    }

    public List<CursoEntity> buscarTodos() throws UsernameNotFoundException {
        return repository.findAll();
    }

    public CursoEntity salvar(CursoEntity cursoEntity) throws UsernameNotFoundException {
        return repository.save(cursoEntity);
    }

    public List<CursoDTO> buscarCursosPorAluno(String email) throws UsernameNotFoundException {
        List<CursoEntity> cursos = repository.buscarCursosPorAluno(email);

        if (Objects.isNull(cursos) || cursos.isEmpty()) {
            log.info("Nenhum curso não encontrado: " + email);
            return emptyList();
        }

        return cursos.stream()
                .map(CursoDTO::fromEntity)
                .toList();
    }

    public CursoDTO validarCursoPorAlunoEhId(String email, Long id) throws UsernameNotFoundException {
        Optional<CursoEntity> curso = repository.buscarCursoPorAlunoEhId(email, id);

        if (curso.isEmpty()) {
            log.info("Curso não encontrado: " + id);
            return null;
        } else {
            return CursoDTO.fromEntity(curso.get());
        }
    }
}
