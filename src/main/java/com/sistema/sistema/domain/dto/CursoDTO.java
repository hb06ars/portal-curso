package com.sistema.sistema.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CursoDTO {
    private Long id;
    private String nome;
    private String descricao;

    public static CursoDTO fromEntity(com.sistema.sistema.domain.entity.CursoEntity curso) {
        return CursoDTO.builder()
                .id(curso.getId())
                .nome(curso.getNome())
                .descricao(curso.getDescricao())
                .build();
    }
}
