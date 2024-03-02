package com.example.dockertest.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ImageController {


    private Path getImagesPath() {
        try {
            return Path.of(
              Objects.requireNonNull(
                  ImageController.class.getClassLoader().getResource("static/images"))
                .toURI().getPath().substring(1));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

//    public Path loadResourceAsPath(String resourceLocation) {
//        Resource resource = resourceLoader.getResource(resourceLocation);
//        try {
//            return Paths.get(resource.getURI());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Sanitize the filename to prevent path traversal
            Path file = getImagesPath().resolve(filename).normalize();

            // Security check - Ensure the file is still within the intended directory
            if (!file.startsWith(getImagesPath())) {
                throw new SecurityException("Attempt to access file outside of the allowed directory");
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                String contentType = Optional.ofNullable(Files.probeContentType(file))
                  .orElse("application/octet-stream"); // Fallback
                return ResponseEntity.ok()
                  .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                  .contentType(MediaType.parseMediaType(contentType))
                  .body(resource);
            } else {
                // Handle the case where the file can't be found or isn't readable
                return ResponseEntity.notFound().build();
            }
        } catch (SecurityException e) {
            // Handle the security exception for path traversal attempts
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle other exceptions
            return ResponseEntity.badRequest().build();
        }
    }
}
