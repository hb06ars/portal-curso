package com.sistema.sistema.app.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
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
import java.io.FileInputStream;
import java.io.OutputStream;
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

    @GetMapping("/stream/{id}")
    public void streamVideo(
            @PathVariable String id,
            @RequestParam String token,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            // valida token
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256("segredo123"))
                    .build()
                    .verify(token);

            if (!jwt.getClaim("videoId").asString().equals(id)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            File videoFile = new ClassPathResource("/videos/aula_" + id + ".mp4").getFile();
            long fileLength = videoFile.length();

            String range = request.getHeader("Range");

            if (range == null) {
                // Sem Range → manda tudo (pouco comum)
                response.setContentType("video/mp4");
                response.setHeader("Content-Length", String.valueOf(fileLength));
                Files.copy(videoFile.toPath(), response.getOutputStream());
                return;
            }

            // --- Suporte a RANGE ---
            long start = 0;
            long end = fileLength - 1;

            String[] ranges = range.replace("bytes=", "").split("-");
            start = Long.parseLong(ranges[0]);

            if (ranges.length > 1 && !ranges[1].isEmpty()) {
                end = Long.parseLong(ranges[1]);
            }

            long chunkSize = end - start + 1;

            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
            response.setContentType("video/mp4");
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            response.setHeader("Content-Length", String.valueOf(chunkSize));

            FileInputStream inputStream = new FileInputStream(videoFile);
            inputStream.skip(start);

            byte[] buffer = new byte[8192];
            long bytesRemaining = chunkSize;

            OutputStream output = response.getOutputStream();

            while (bytesRemaining > 0) {
                int bytesToRead = (int) Math.min(buffer.length, bytesRemaining);
                int bytesRead = inputStream.read(buffer, 0, bytesToRead);

                if (bytesRead == -1) break;

                output.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }

            output.flush();
            inputStream.close();

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }


}
