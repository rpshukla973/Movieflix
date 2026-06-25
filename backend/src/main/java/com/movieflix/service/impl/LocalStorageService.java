package com.movieflix.service.impl;

import com.movieflix.exception.ApiException;
import com.movieflix.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalStorageService implements StorageService {

    private final Path rootPath;

    public LocalStorageService(@Value("${app.storage.local-path}") String localPath) {
        this.rootPath = Path.of(localPath);
    }

    @Override
    public String store(MultipartFile file, String folder) {
        try {
            Path dir = rootPath.resolve(folder);
            Files.createDirectories(dir);
            String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
            Path target = dir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return folder + "/" + fileName;
        } catch (IOException ex) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store file");
        }
    }

    @Override
    public Resource loadAsResource(String filePath) {
        try {
            if (filePath == null || filePath.isBlank()) {
                throw new ApiException(HttpStatus.NOT_FOUND, "File not found");
            }

            Path basePath = rootPath.toAbsolutePath().normalize();
            String normalizedPath = normalizeStoredPath(filePath);
            Path candidatePath = Path.of(normalizedPath);
            Path resolvedPath = candidatePath.isAbsolute()
                    ? candidatePath.normalize()
                    : basePath.resolve(normalizedPath).normalize();

            if (!resolvedPath.startsWith(basePath)) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid file path");
            }

            Resource resource = new UrlResource(resolvedPath.toUri());
            if (!resource.exists()) {
                throw new ApiException(HttpStatus.NOT_FOUND, "File not found");
            }
            return resource;
        } catch (Exception ex) {
            if (ex instanceof ApiException apiException) {
                throw apiException;
            }
            throw new ApiException(HttpStatus.NOT_FOUND, "File not found");
        }
    }

    private String normalizeStoredPath(String filePath) {
        String normalized = filePath.replace('\\', '/');
        normalized = normalized.replaceFirst("^\\./", "");
        normalized = normalized.replaceFirst("^/", "");
        if (normalized.startsWith("storage/")) {
            normalized = normalized.substring("storage/".length());
        }
        return normalized;
    }
}
