package com.hipo.domain.processor;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileProcessor {

    public String storeFile(MultipartFile multipartFile, String path) throws IOException {

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

    public String extracted(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos);
    }
}
