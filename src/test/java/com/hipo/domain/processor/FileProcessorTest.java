package com.hipo.domain.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileProcessorTest {

    FileProcessor fileProcessor = new FileProcessor();
    String profileImgPath =  "/Users/hipo/Desktop/hipo/src/test/resources/static/profile_img/";

    @Test
    @DisplayName("storeFile 테스트")
    void storeFile_Test() throws IOException {
        String fileName = "hipo.jpeg";
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        String storeFullPath = fileProcessor.storeFile(file, profileImgPath);

        int pos = storeFullPath.lastIndexOf("/");
        int dotPos = storeFullPath.lastIndexOf(".");

        String fullPath = storeFullPath.substring(0, pos + 1);
        String uuid = storeFullPath.substring(pos + 1, dotPos);
        String ext = storeFullPath.substring(dotPos + 1);

        assertThat(fullPath).isEqualTo(profileImgPath);
        assertThat(uuid.length()).isGreaterThan(0);
        assertThat(ext).isEqualTo("jpeg");
    }

    @Test
    @DisplayName("storeFile 테스트 - 파일 이름 공백")
    void storeFile_nameEmpty_Test() throws IOException {
        String fileName = ".jpeg";
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        String storeFullPath = fileProcessor.storeFile(file, profileImgPath);

        int pos = storeFullPath.lastIndexOf("/");
        int dotPos = storeFullPath.lastIndexOf(".");

        String fullPath = storeFullPath.substring(0, pos + 1);
        String uuid = storeFullPath.substring(pos, dotPos);
        String ext = storeFullPath.substring(dotPos + 1);

        assertThat(fullPath).isEqualTo(profileImgPath);
        assertThat(uuid.length()).isGreaterThan(0);
        assertThat(ext).isEqualTo("jpeg");
    }

    @Test
    @DisplayName("storeFile 테스트 - 확장자 없음")
    void storeFile_extNull_Test() throws IOException {
        String fileName = "peg";
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        assertThrows(IllegalArgumentException.class, () -> fileProcessor.storeFile(file, profileImgPath));
    }

    @Test
    @DisplayName("storeFile 테스트 - 확장자 구분자만 있음")
    void storeFile_extSeparatorOnly_Test() throws IOException {
        String fileName = ".";
        MockMultipartFile file = new MockMultipartFile("image", fileName, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/main/resources/static/sample/ea451747-9643" +
                        "-470f-9605-81407fc077c7.jpeg"));

        assertThrows(IllegalArgumentException.class, () -> fileProcessor.storeFile(file, profileImgPath));
    }
}