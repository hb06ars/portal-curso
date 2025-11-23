package com.sistema.sistema.infra.repository;


import com.sistema.sistema.domain.entity.CursoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CursoRepository extends JpaRepository<CursoEntity, Long> {

    @Query(
            value = """
                    select c.id, c.nome, c.descricao
                    from aluno a
                    inner join aluno_curso ac on ac.aluno_id = a.id
                    inner join curso c on c.id = ac.curso_id
                    where a.username = :email
                    """,
            nativeQuery = true
    )
    List<CursoEntity> buscarCursosPorAluno(@Param("email") String email);


    @Query(
            value = """
                    select c.id, c.nome, c.descricao 
                    from curso c
                    inner join aluno_curso ac on ac.curso_id = c.id 
                    inner join aluno a on a.id = ac.aluno_id 
                    where a.username = :email and c.id = :id
                    """,
            nativeQuery = true
    )
    Optional<CursoEntity> buscarCursoPorAlunoEhId(@Param("email") String email, @Param("id") Long id);

}
