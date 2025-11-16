package com.sistema.sistema.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@ToString
@Table(name = "aluno")
public class AlunoEntity {

    @Id
    @SequenceGenerator(name = "aluno_id_seq", sequenceName = "aluno_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nome;

    @Column(nullable = true, length = 255, unique = true)
    private String username;

    @Column
    private String password;

    @Column
    private String role;

    @ManyToMany
    @JoinTable(
            name = "aluno_curso",
            joinColumns = @JoinColumn(name = "aluno_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private Set<CursoEntity> cursos = new HashSet<>();

    public AlunoEntity(Long id, String nome, String username, String password, String role, Set<CursoEntity> cursos) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.role = role;
        this.cursos = cursos;
    }

    @Override
    public String toString() {
        return "AlunoEntity{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", cursos=" + cursos.stream()
                .map(c -> "{id=" + c.getId() + ", nome=" + c.getNome() + "}")
                .toList()
                +
                '}';
    }

}