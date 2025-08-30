package org.alumnet.archive.lib.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.alumnet.archive.lib.services.interfaces.IStorageService;

import java.io.IOException;

@Controller
@RequestMapping("/library")
public class LibraryController {

    private final IStorageService storageService;

    public LibraryController(@Qualifier("awsStorageService") IStorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Endpoint para subir un archivo.
     * Espera un archivo en la petición y el nombre que se le dará en S3.
     *
     * @param file El archivo a subir.
     * @param fileName El nombre que tendrá el archivo en S3.
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("fileName") String fileName) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Por favor, selecciona un archivo para subir.");
        }
        try {
            String url = storageService.uploadFile(file, fileName);
            return ResponseEntity.status(HttpStatus.CREATED).body("Archivo subido exitosamente. URL: " + url);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("No se pudo subir el archivo: " + e.getMessage());
        }
    }

    /**
     * Endpoint para descargar un archivo.
     *
     * @param fileName El nombre del archivo en S3.
     * @return ResponseEntity con el archivo como un recurso de bytes.
     */
    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestParam String fileName) {
        try {
            byte[] data = storageService.downloadFile(fileName);
            ByteArrayResource resource = new ByteArrayResource(data);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .contentLength(data.length)
                    .body(resource);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }
    }
}
