package com.moneyware.bank.documentservice.controller;

import com.moneyware.bank.documentservice.entity.FilesEntity;
import com.moneyware.bank.documentservice.exception.CustomException;
import com.moneyware.bank.documentservice.service.FileService;
import com.moneyware.bank.documentservice.upload.UploadGateway;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/file")
@CrossOrigin()
@Slf4j
@SecurityRequirement(name = "Bearer Authentication")
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    private UploadGateway gateway;

    @PostMapping(value = "/upload")
    public FilesEntity uploadFile(@RequestParam("file") MultipartFile file,
                                  @RequestParam("type") String documentType, @RequestParam("customerId") int customerId) {
        try {
            return fileService.saveFile(file, documentType, customerId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException();
        }
    }
}
