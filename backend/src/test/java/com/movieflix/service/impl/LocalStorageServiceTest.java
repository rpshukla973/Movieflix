package com.movieflix.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocalStorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void loadAsResourceAcceptsAbsolutePathWithinRoot() throws Exception {
        Path videoFile = tempDir.resolve("videos").resolve("sample.mp4");
        Files.createDirectories(videoFile.getParent());
        Files.writeString(videoFile, "video-bytes");

        LocalStorageService service = new LocalStorageService(tempDir.toString());

        Resource resource = service.loadAsResource(videoFile.toString());

        assertTrue(resource.exists());
        try (var inputStream = resource.getInputStream()) {
            assertEquals("video-bytes", new String(inputStream.readAllBytes(), StandardCharsets.UTF_8));
        }
    }
}
