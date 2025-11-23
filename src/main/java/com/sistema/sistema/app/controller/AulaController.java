package com.sistema.sistema.app.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

@Controller
@RequestMapping("/aula")
public class AulaController {

    private final Algorithm algorithm = Algorithm.HMAC256("secreto-muito-forte-ALTERE-AGORA");

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String cursoPage() {
        return "aula";
    }


    // Gera URL temporária para o playlist (chamado pelo frontend)
    @GetMapping("/{id}/token")
    @ResponseBody
    public String gerarLinkTemporario(@PathVariable String id,
                                      @AuthenticationPrincipal UserDetails user) {
        String token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("videoId", id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000)) // 5 minutos
                .sign(algorithm);

        // retorna a rota que o player irá consumir
        return "/aula/hls/" + id + "/playlist.m3u8?token=" + token;
    }

    @GetMapping(value = "/hls/{id}/playlist.m3u8", produces = "application/vnd.apple.mpegurl")
    public void getPlaylist(@PathVariable String id, @RequestParam String token, HttpServletResponse response) throws IOException {
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        if (!id.equals(jwt.getClaim("videoId").asString())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // Lista os arquivos TS da pasta
        String baseDir = System.getProperty("user.dir") + "/src/main/resources/videos/hls/aula_" + id + "/";
        File dir = new File(baseDir);
        File[] tsFiles = dir.listFiles((d, name) -> name.endsWith(".ts"));

        response.setContentType("application/vnd.apple.mpegurl");
        StringBuilder playlist = new StringBuilder("#EXTM3U\n#EXT-X-VERSION:3\n#EXT-X-TARGETDURATION:10\n");

        for (File ts : tsFiles) {
            playlist.append("#EXTINF:10.0,\n")
                    .append(ts.getName())
                    .append("?token=").append(token)
                    .append("\n");
        }
        playlist.append("#EXT-X-ENDLIST\n");
        response.getWriter().write(playlist.toString());
        response.flushBuffer();
    }


    // Serve segments (.ts) — opcional: você pode optar por servir .ts via controller protegido ou deixar público.
    @GetMapping(value = "/hls/{id}/aula_{seq}.ts", produces = "video/MP2T")
    public void getSegment(@PathVariable String id,
                           @PathVariable String seq,
                           @RequestParam String token,
                           HttpServletResponse response) throws IOException {

        // token check
        DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
        if (!id.equals(jwt.getClaim("videoId").asString())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        // caminho absoluto do filesystem
        String baseDir = System.getProperty("user.dir"); // raiz do projeto
        File file = new File(baseDir + "/src/main/resources/videos/hls/aula_" + id + "/aula_" + seq + ".ts");

        if (!file.exists()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("video/MP2T");
        Files.copy(file.toPath(), response.getOutputStream());
        response.flushBuffer();
    }



    // Serve chave (enc.key) — MUITO IMPORTANTE: exige token + token deve ser curto
    @GetMapping(value = "/hls/key/{id}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void getKey(@PathVariable String id,
                       @RequestParam String token,
                       HttpServletResponse response) throws Exception {
        try {
            DecodedJWT jwt = JWT.require(algorithm).build().verify(token);
            if (!id.equals(jwt.getClaim("videoId").asString())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            Resource res = new ClassPathResource("/videos/hls/aula_" + id + "/enc.key");
            File file = res.getFile();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            Files.copy(file.toPath(), response.getOutputStream());
            response.flushBuffer();
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
