package com.sistema.sistema.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/aula")
public class AulaController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String cursoPage(@AuthenticationPrincipal UserDetails user) {
        System.out.println("Olá, " + (user != null ? user.getUsername() : "visitante") + " — endpoint protegido!");
        return "aula";
    }
}

