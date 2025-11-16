package com.sistema.sistema.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
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

    @Column
    private Long cursoId;

    public AlunoEntity(Long id, String nome, String username, String password, String role, Long cursoId) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.password = password;
        this.role = role;
        this.cursoId = cursoId;
    }

}