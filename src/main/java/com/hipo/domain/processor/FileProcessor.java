package com.hipo.domain.processor;

import com.hipo.exception.IllegalRequestException;
import org.springframework.http.MediaType;
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
        return storeFilename;
    }

    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extracted(originalFilename);
        return uuid + "." + ext;
    }

    public String extracted(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    public String getFullPath(String path, String storeFilename) {
        return path + storeFilename;
    }

    public String getMediaType(String fileName) {
        String extracted = extracted(fileName);
        if (extracted.equals("jpeg") || extracted.equals("JPEG")) {
            return MediaType.IMAGE_JPEG_VALUE;
        }
        if (extracted.equals("png") || extracted.equals("PNG")) {
            return MediaType.IMAGE_PNG_VALUE;
        }
        throw new IllegalRequestException("확장자가 jpeg 또는 png가 아닙니다.");
    }
}
