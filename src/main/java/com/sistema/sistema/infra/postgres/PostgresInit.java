package com.sistema.sistema.infra.postgres;

import com.sistema.sistema.domain.entity.AlunoEntity;
import com.sistema.sistema.domain.entity.CursoEntity;
import com.sistema.sistema.domain.enums.PerfilEnum;
import com.sistema.sistema.domain.service.AlunoService;
import com.sistema.sistema.domain.service.CursoService;
import com.sistema.sistema.infra.config.security.PasswordConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;

@Configuration
@Slf4j
public class PostgresInit {

    private final PasswordConfig passwordConfig;
    private final AlunoService alunoService;
    private final CursoService cursoService;

    public PostgresInit(PasswordConfig passwordConfig, AlunoService alunoService, CursoService cursoService) {
        this.passwordConfig = passwordConfig;
        this.alunoService = alunoService;
        this.cursoService = cursoService;
    }

    @PostConstruct
    public void initUser() {
        log.info("Verificando existência de usuários...");

        inserirCursoInicial();
        inserirAdmInicial();

    }

    private CursoEntity inserirCursoInicial() {
        if (cursoService.findAll().isEmpty()) {
            log.info("Nenhum curso encontrado. Criando curso padrão...");
            String nomeCurso = "Java";
            String descricaoCurso = "Desenvolvimento Java";

            return cursoService.save(CursoEntity.builder()
                    .nome(nomeCurso)
                    .descricao(descricaoCurso)
                    .build());
        } else {
            log.info("Curso já cadastrado. Nenhuma ação necessária.");
            return null;
        }
    }

    private AlunoEntity inserirAdmInicial() {
        if (alunoService.findAll().isEmpty()) {
            log.info("Nenhum usuário encontrado. Criando usuário padrão...");
            String usrDefault = "hb06ars@mail.com";
            String passDefault = "123";

            return alunoService.save(AlunoEntity.builder()
                    .username(usrDefault)
                    .nome("Fulano")
                    .password(passwordConfig.encoder().encode(passDefault))
                    .cursos(new HashSet<>(cursoService.findAll()))
                    .role(PerfilEnum.ADMIN.getDescricao())
                    .build());
        } else {
            log.info("Usuários já cadastrados. Nenhuma ação necessária.");
            return null;
        }
    }

}
