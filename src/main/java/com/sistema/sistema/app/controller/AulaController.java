package com.sistema.sistema.app.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.nio.file.Files;
import java.util.Date;

@Controller
@RequestMapping("/aula")
public class AulaController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String cursoPage() {
        return "aula";
    }

    // Gera a URL temporária
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> gerarLinkTemporario(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails user) {

        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("videoId", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 min
                .sign(Algorithm.HMAC256("segredo123"));

        // URL correta
        String urlTemporaria = "/aula/stream/" + id + "?token=" + token;

        return ResponseEntity.ok(urlTemporaria);
    }

    // Faz o streaming do vídeo
    @GetMapping("/stream/{id}")
    public void streamVideo(
            @PathVariable String id,
            @RequestParam String token,
            HttpServletResponse response) throws Exception {

        try {
            // valida token
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256("segredo123"))
                    .build()
                    .verify(token);

            // confirma que o token corresponde ao vídeo
            if (!jwt.getClaim("videoId").asString().equals(id)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            ClassPathResource resource = new ClassPathResource("/videos/aula_" + id + ".mp4");
            File arquivo = resource.getFile();

            response.setContentType("video/mp4");
            response.setHeader("Content-Length", String.valueOf(arquivo.length()));

            Files.copy(arquivo.toPath(), response.getOutputStream());
            response.flushBuffer();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
