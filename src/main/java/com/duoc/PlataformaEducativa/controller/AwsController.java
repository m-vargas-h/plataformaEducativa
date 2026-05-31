package com.duoc.PlataformaEducativa.controller;

import com.duoc.PlataformaEducativa.model.Asset;
import com.duoc.PlataformaEducativa.service.AwsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class AwsController {

    private final AwsService awsService;

    // POST /storage/upload/{folder}
    @PostMapping("/upload/{folder}")
    public ResponseEntity<Map<String, String>> upload(
            @PathVariable String folder,
            @RequestParam("file") MultipartFile file) throws IOException {

        Asset asset = new Asset();
        asset.setFolder(folder);
        asset.setFileName(file.getOriginalFilename());
        asset.setContentType(file.getContentType());
        asset.setContent(file.getBytes());

        awsService.upload(asset);

        return ResponseEntity.ok(Map.of(
            "mensaje", "Archivo subido exitosamente",
            "ruta", folder + "/" + file.getOriginalFilename()
        ));
    }

    // GET /storage/download/{folder}/{fileName}
    @GetMapping("/download/{folder}/{fileName}")
    public ResponseEntity<byte[]> download(
            @PathVariable String folder,
            @PathVariable String fileName) {

        Asset asset = awsService.download(folder, fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(asset.getContent());
    }

    // PUT /storage/move/{folder}
    @PutMapping("/move/{folder}")
    public ResponseEntity<Map<String, String>> move(
            @PathVariable String folder,
            @RequestParam String oldFileName,
            @RequestParam String newFileName) {

        awsService.move(folder, oldFileName, newFileName);

        return ResponseEntity.ok(Map.of(
            "mensaje", "Archivo movido exitosamente",
            "ruta", folder + "/" + newFileName
        ));
    }

    // DELETE /storage/delete/{folder}/{fileName}
    @DeleteMapping("/delete/{folder}/{fileName}")
    public ResponseEntity<Map<String, String>> delete(
            @PathVariable String folder,
            @PathVariable String fileName) {

        awsService.delete(folder, fileName);

        return ResponseEntity.ok(Map.of(
            "mensaje", "Archivo eliminado exitosamente"
        ));
    }
}
