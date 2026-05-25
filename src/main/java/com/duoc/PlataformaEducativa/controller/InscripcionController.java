package com.duoc.PlataformaEducativa.controller;

import com.duoc.PlataformaEducativa.model.Inscripcion;
import com.duoc.PlataformaEducativa.service.InscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    // POST /inscripciones
    @PostMapping
    public ResponseEntity<Inscripcion> crearInscripcion(
            @RequestBody Map<String, List<Long>> body,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = usuarioRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow()
                .getId();

        List<Long> cursoIds = body.get("cursoIds");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inscripcionService.crearInscripcion(usuarioId, cursoIds));
    }

    // GET /inscripciones/mis-inscripciones
    @GetMapping("/mis-inscripciones")
    public ResponseEntity<List<Inscripcion>> misInscripciones(
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = usuarioRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow()
                .getId();

        return ResponseEntity.ok(inscripcionService.listarInscripcionesPorUsuario(usuarioId));
    }

    // DELETE /inscripciones/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarInscripcion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long usuarioId = usuarioRepository
                .findByEmail(userDetails.getUsername())
                .orElseThrow()
                .getId();

        inscripcionService.eliminarInscripcion(id, usuarioId);
        return ResponseEntity.ok(Map.of("mensaje", "Inscripción eliminada exitosamente"));
    }
}