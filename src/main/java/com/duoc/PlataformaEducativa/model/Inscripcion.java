package com.duoc.PlataformaEducativa.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToMany
    @JoinTable(
        name = "inscripcion_curso",
        joinColumns = @JoinColumn(name = "inscripcion_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPagar;

    @Column(nullable = false)
    private LocalDateTime fechaInscripcion;

    @PrePersist
    protected void onCreate() {
        fechaInscripcion = LocalDateTime.now();
    }
}