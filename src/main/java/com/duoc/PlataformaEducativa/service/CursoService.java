package com.duoc.PlataformaEducativa.service;

import com.duoc.PlataformaEducativa.exception.ResourceNotFoundException;
import com.duoc.PlataformaEducativa.model.Curso;
import com.duoc.PlataformaEducativa.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository cursoRepository;

    // GET /cursos
    public List<Curso> listarCursos() {
        return cursoRepository.findAll();
    }

    // GET /cursos/{id}
    public Curso obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso no encontrado con id: " + id));
    }

    // POST /cursos
    public Curso crearCurso(Curso curso) {
        if (cursoRepository.existsByNombre(curso.getNombre())) {
            throw new IllegalArgumentException("Ya existe un curso con el nombre: " + curso.getNombre());
        }
        return cursoRepository.save(curso);
    }

    // PUT /cursos/{id}
    public Curso actualizarCurso(Long id, Curso cursoActualizado) {
        Curso curso = obtenerCursoPorId(id);
        curso.setNombre(cursoActualizado.getNombre());
        curso.setInstructor(cursoActualizado.getInstructor());
        curso.setDuracion(cursoActualizado.getDuracion());
        curso.setCosto(cursoActualizado.getCosto());
        return cursoRepository.save(curso);
    }

    // DELETE /cursos/{id}
    public void eliminarCurso(Long id) {
        Curso curso = obtenerCursoPorId(id);
        cursoRepository.delete(curso);
    }
}
