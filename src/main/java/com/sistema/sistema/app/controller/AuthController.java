package com.sistema.sistema.app.controller;

import com.sistema.sistema.domain.dto.MessageDTO;
import com.sistema.sistema.domain.request.AuthRequest;
import com.sistema.sistema.infra.config.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid AuthRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.username(), request.password());
            Authentication authentication = authenticationManager.authenticate(authToken);
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {
                String jwt = jwtUtil.generateToken(userDetails);

                Map<String, Object> body = new HashMap<>();
                body.put("token", "Bearer " + jwt);
                body.put("expiresIn", jwtUtil.getExpirationMs());
                body.put("username", ((UserDetails) principal).getUsername());

                return ResponseEntity.ok(body);
            } else {
                Map<String, Object> body = new HashMap<>();
                body.put("error", "Principal inválido");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
            }

        } catch (AuthenticationException ex) {
            return ResponseEntity
                    .status(401)
                    .body(MessageDTO.builder()
                            .mensagem("Usuário ou senha inválidos")
                            .build());
        }
    }
}
