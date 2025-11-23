package com.sistema.sistema.app.controller;

import com.sistema.sistema.domain.dto.MessageDTO;
import com.sistema.sistema.domain.service.AlunoService;
import com.sistema.sistema.infra.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testeapi")
public class TestApiController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping("/autenticado")
    @PreAuthorize("isAuthenticated()")
    public String hello(@AuthenticationPrincipal UserDetails user) {
        return "Olá, " + (user != null ? user.getUsername() : "visitante") + " — endpoint protegido!";
    }

    @GetMapping("/logado")
    @PreAuthorize("isAuthenticated()")
    public String buscarCustomUserDetails() {
        return "Logado";
    }

    @PreAuthorize("hasAnyRole('AMIN', 'ADM_PORTAL')")
    @GetMapping("/testeadm")
    public String testeadm() {
        return "AMIN";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/aluno")
    public ResponseEntity<MessageDTO> aluno(@AuthenticationPrincipal UserDetails user) {
        if (user != null) {
            var aluno = alunoService.buscarAlunoPorEmail(user.getUsername()).orElse(null);
            return ResponseEntity
                    .ok(MessageDTO.builder().mensagem(aluno != null ? aluno.toString() : "").build());
        }
        throw new ObjectNotFoundException("Usuário não encontrado.");
    }

}
