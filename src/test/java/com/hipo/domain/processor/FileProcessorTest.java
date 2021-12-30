package com.hipo.domain.processor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class FileProcessorTest {

    FileProcessor fileProcessor = new FileProcessor();
    String profileImgPath =  "/Users/hipo/Desktop/hipo/src/test/resources/static/profile_img/";

    @Test
    @DisplayName("storeFile 테스트 성공")
    void storeFile_Test() throws IOException {

        //given
        String extract = "jpeg";
        MockMultipartFile file = new MockMultipartFile("image", "testFilename." + extract, "image/jpeg",
                new FileInputStream("/Users/hipo/Desktop/hipo/src/test/resources/static/testProfileImg.jpeg"));

        //when
        String storeFullPath = fileProcessor.storeFile(file, profileImgPath);

        int pos = storeFullPath.lastIndexOf("/");
        int dotPos = storeFullPath.lastIndexOf(".");

        String fullPath = storeFullPath.substring(0, pos + 1);
        String uuid = storeFullPath.substring(pos + 1, dotPos);
        String storeExtract = storeFullPath.substring(dotPos + 1);

        //then
        assertThat(fullPath).isEqualTo(profileImgPath);
        assertThat(uuid).isNotEmpty();
        assertThat(uuid).isNotBlank();
        assertThat(storeExtract).isEqualTo(extract);
    }
}