package com.dailymission.api.springboot.web.common;


import lombok.Builder;
import org.springframework.mock.web.MockMultipartFile;

import java.io.FileInputStream;

public class MultipartFileSetup {

    @Builder
    public MultipartFileSetup(){

    }

    public MockMultipartFile get() throws Exception {
        return buildMultipartFile();
    }

    private MockMultipartFile buildMultipartFile() throws Exception {
        // given
        return new MockMultipartFile("file",
                "file.jpg",
                "multipart/form-data",
                new FileInputStream("C:/portfolio/daily-mission/src/test/resources/로고.jpg"));
    }
}
