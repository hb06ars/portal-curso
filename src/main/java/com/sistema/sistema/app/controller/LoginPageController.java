package com.sistema.sistema.app.controller;

import com.sistema.sistema.domain.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LoginPageController {

    @Autowired
    CursoService service;

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public String homePage(@AuthenticationPrincipal UserDetails user, Model model) {
        var cursos = service.buscarCursosPorAluno(user.getUsername());
        model.addAttribute("cursos", cursos);
        return "home";
    }


    @GetMapping("/curso/{id}")
    @PreAuthorize("isAuthenticated()")
    public String cursoPage(@AuthenticationPrincipal UserDetails user,
                            @PathVariable Long id, Model model) {
        var curso = service.validarCursoPorAlunoEhId(user.getUsername(), id);
        model.addAttribute("curso", curso);
        return "curso";
    }
}

