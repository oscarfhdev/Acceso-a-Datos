package com.primerProyecto.PrimerProyecto.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "peliculas")
@Data
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 120)
    private String titulo;

    private int duracion;

    @Column(name = "fecha_estreno")
    private LocalDate fechaEstreno;

    private String sinopsis;
}
