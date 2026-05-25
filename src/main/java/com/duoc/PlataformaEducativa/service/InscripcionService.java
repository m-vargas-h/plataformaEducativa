package com.duoc.PlataformaEducativa.service;

import com.duoc.PlataformaEducativa.exception.ResourceNotFoundException;
import com.duoc.PlataformaEducativa.model.Curso;
import com.duoc.PlataformaEducativa.model.Inscripcion;
import com.duoc.PlataformaEducativa.model.Usuario;
import com.duoc.PlataformaEducativa.repository.CursoRepository;
import com.duoc.PlataformaEducativa.repository.InscripcionRepository;
import com.duoc.PlataformaEducativa.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final CursoRepository cursoRepository;
    private final UsuarioRepository usuarioRepository;

    // POST /inscripciones
    public Inscripcion crearInscripcion(Long usuarioId, List<Long> cursoIds) {
        // Buscar usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + usuarioId));

        // Buscar cursos
        List<Curso> cursos = cursoRepository.findAllById(cursoIds);
        if (cursos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron cursos con los IDs proporcionados");
        }

        // Calcular total
        BigDecimal total = cursos.stream()
                .map(Curso::getCosto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Crear y guardar inscripcion
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuario(usuario);
        inscripcion.setCursos(cursos);
        inscripcion.setTotalPagar(total);

        return inscripcionRepository.save(inscripcion);
    }

    // GET /inscripciones/usuario/{usuarioId}
    public List<Inscripcion> listarInscripcionesPorUsuario(Long usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId);
    }

    // DELETE /inscripciones/{id}
    public void eliminarInscripcion(Long id, Long usuarioId) {
        Inscripcion inscripcion = inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inscripción no encontrada con id: " + id));

        // Verificar que la inscripcion pertenece al usuario
        if (!inscripcion.getUsuario().getId().equals(usuarioId)) {
            throw new SecurityException("No tienes permiso para eliminar esta inscripción");
        }

        inscripcionRepository.delete(inscripcion);
    }
}