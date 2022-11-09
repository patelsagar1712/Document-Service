package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.repository.FileRepository;
import com.moneyware.bank.documentservice.security.JwtUtil;
import com.moneyware.bank.documentservice.upload.FileUploadConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {FileServiceImpl.class})
@ExtendWith(SpringExtension.class)
class FileServiceImplTest {

    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileServiceImpl fileService;

    @MockBean
    private FileUploadConfig fileUploadConfig;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private Path path;

    private static String DIRECTORY_NAME = "Test";

    @Test
    void upload() {
        when(fileRepository.findAll()).thenReturn(new ArrayList<>());
        fileService.upload();
        verify(fileRepository).findAll();
        assertEquals("${app.file.upload-dir}", fileService.fileStorageLocation);
    }


}