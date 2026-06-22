package com.duoc.PlataformaEducativa.controller;

import com.duoc.PlataformaEducativa.model.Asset;
import com.duoc.PlataformaEducativa.model.Inscripcion;
import com.duoc.PlataformaEducativa.service.AwsService;
import com.duoc.PlataformaEducativa.service.InscripcionService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.duoc.PlataformaEducativa.repository.UsuarioRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final UsuarioRepository usuarioRepository;
    private final AwsService awsService;

    // POST /inscripciones
    @PostMapping
    public ResponseEntity<Inscripcion> crearInscripcion(
            @RequestBody Map<String, List<Long>> body,
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaimAsString("emails") != null
                ? jwt.getClaimAsString("emails")
                : jwt.getSubject();

        Long usuarioId = usuarioRepository
                .findByEmail(email)
                .orElseThrow()
                .getId();

        List<Long> cursoIds = body.get("cursoIds");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.crearInscripcion(usuarioId, cursoIds));
    }

    // GET /inscripciones/mis-inscripciones
    @GetMapping("/mis-inscripciones")
    public ResponseEntity<List<Inscripcion>> misInscripciones(
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaimAsString("emails") != null
                ? jwt.getClaimAsString("emails")
                : jwt.getSubject();

        Long usuarioId = usuarioRepository
                .findByEmail(email)
                .orElseThrow()
                .getId();

        return ResponseEntity.ok(inscripcionService.listarInscripcionesPorUsuario(usuarioId));
    }

    // DELETE /inscripciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInscripcion(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaimAsString("emails") != null
                ? jwt.getClaimAsString("emails")
                : jwt.getSubject();

        Long usuarioId = usuarioRepository
                .findByEmail(email)
                .orElseThrow()
                .getId();

        inscripcionService.eliminarInscripcion(id, usuarioId);
        return ResponseEntity.ok(Map.of("mensaje", "Inscripción eliminada exitosamente"));
    }

    // GET /inscripciones/{id}/resumen
    @GetMapping("/{id}/resumen")
    public ResponseEntity<byte[]> descargarResumen(@PathVariable Long id) {
        byte[] contenido = inscripcionService.generarResumen(id);
        String fileName = "resumen.txt";

        Asset asset = new Asset();
        asset.setFolder(String.valueOf(id));
        asset.setFileName(fileName);
        asset.setContentType("text/plain");
        asset.setContent(contenido);
        awsService.upload(asset);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(contenido);
    }
}