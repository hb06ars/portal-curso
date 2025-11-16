package com.sistema.sistema.domain.enums;

import lombok.Getter;

@Getter
public enum PerfilEnum {

    ADMIN(0, "ADMIN"),
    FUNCIONARIO(1, "FUNCIONARIO"),
    ALUNO(2, "ALUNO");

    private Integer codigo;
    private String descricao;

    private PerfilEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static PerfilEnum toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }

        for (PerfilEnum x : PerfilEnum.values()) {
            if (cod.equals(x.getCodigo())) {
                return x;
            }
        }

        throw new IllegalArgumentException("Perfil inválido");
    }
}
