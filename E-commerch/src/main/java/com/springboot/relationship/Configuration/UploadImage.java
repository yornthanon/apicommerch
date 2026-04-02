package com.springboot.relationship.Configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@Configuration
public class UploadImage implements WebMvcConfigurer {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String SaveImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Path path = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (Files.notExists(path)) {
                Files.createDirectories(path);
            }

            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
            String extension = "";
            int dotIndex = originalFileName.lastIndexOf('.');
            if (dotIndex != -1) {
                extension = originalFileName.substring(dotIndex);
            }

            String filename = UUID.randomUUID() + extension;
            Path filePath = path.resolve(filename);
            file.transferTo(filePath);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("file is not to upload");
        }


    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            if (Files.notExists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }

        registry.addResourceHandler("/upload/**")
                .addResourceLocations(uploadPath.toUri().toString());
    }
}