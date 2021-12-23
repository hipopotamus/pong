package com.hipo.domain.processor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Component
public class FileProcessor {

    public String storeFile(MultipartFile multipartFile, String path) throws IOException {
        if (multipartFile == null) {
            throw new IllegalArgumentException("MultipartFile이 null입니다.");
        }
        if (Objects.equals(multipartFile.getOriginalFilename(), ".")) {
            throw new IllegalArgumentException("파일 이름이 잘못된 형식입니다.");
        }

        String storeFilename = createStoreFileName(multipartFile.getOriginalFilename());
        String fullPath = getFullPath(path, storeFilename);

        multipartFile.transferTo(new File(fullPath));
        return fullPath;
    }

    private String getFullPath(String path, String storeFilename) {
        return path + storeFilename;
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extracted(originalFilename);

        return uuid + ext;
    }

    private String extracted(String originalFilename) {
        try {
            int pos = originalFilename.lastIndexOf(".");
            return originalFilename.substring(pos);
        } catch (Exception e) {
            throw new IllegalArgumentException("확장자가 없거나 잘못된 형식입니다.");
        }
    }
}
