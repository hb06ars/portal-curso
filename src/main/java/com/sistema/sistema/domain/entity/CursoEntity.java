package com.sistema.sistema.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "curso")
public class CursoEntity {

    @Id
    @SequenceGenerator(name = "curso_id_seq", sequenceName = "curso_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column
    private String descricao;

    @ManyToMany(mappedBy = "cursos")
    private Set<AlunoEntity> alunos = new HashSet<>();

    public CursoEntity(Long id, String nome, String descricao, Set<AlunoEntity> alunos) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.alunos = alunos;
    }

}