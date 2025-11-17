package com.sistema.sistema.app.controller;

import com.sistema.sistema.infra.config.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private static final String LOGIN = "login";
    private static final String ERRO = "error";
    private static final String USUARIO_OU_SENHA_INVALIDO = "Usuário ou senha inválidos";

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return LOGIN;
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            Model model
    ) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authToken);
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String jwt = jwtUtil.generateToken(userDetails);
                model.addAttribute("token", jwt);
                model.addAttribute("username", userDetails.getUsername());
                return "redirect:/home";
            } else {
                model.addAttribute(ERRO, USUARIO_OU_SENHA_INVALIDO);
                return LOGIN;
            }

        } catch (Exception ex) {
            model.addAttribute(ERRO, USUARIO_OU_SENHA_INVALIDO);
            return LOGIN;
        }
    }
}
