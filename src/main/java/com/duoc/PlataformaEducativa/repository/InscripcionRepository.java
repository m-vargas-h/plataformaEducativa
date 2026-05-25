package com.duoc.PlataformaEducativa.repository;

import com.duoc.PlataformaEducativa.model.Inscripcion;
import com.duoc.PlataformaEducativa.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    List<Inscripcion> findByUsuario(Usuario usuario);

    List<Inscripcion> findByUsuarioId(Long usuarioId);
}