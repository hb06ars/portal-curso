package com.sistema.sistema.app.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testeapi")
public class HelloController {

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

}
