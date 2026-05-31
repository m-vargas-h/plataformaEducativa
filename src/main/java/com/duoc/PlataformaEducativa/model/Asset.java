package com.duoc.PlataformaEducativa.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Asset {

    private String fileName;      // nombre del archivo (ej: "resumen.txt")
    private String folder;        // carpeta en el bucket (ej: "1" para inscripción id=1)
    private String contentType;   // tipo MIME (ej: "text/plain")
    private byte[] content;       // contenido del archivo en bytes
}