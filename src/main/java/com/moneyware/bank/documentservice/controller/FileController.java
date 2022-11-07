package com.moneyware.bank.documentservice.controller;

import com.moneyware.bank.documentservice.dto.ResponseDTO;
import com.moneyware.bank.documentservice.entity.FilesEntity;
import com.moneyware.bank.documentservice.service.FileService;
import com.moneyware.bank.documentservice.upload.UploadGateway;
import com.moneyware.bank.documentservice.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

import static com.moneyware.bank.documentservice.constants.MessageConstants.FILE_UPLOAD_ERROR_MESSAGE;
import static com.moneyware.bank.documentservice.constants.MessageConstants.FILE_UPLOAD_SUCCESS_MESSAGE;

@RestController
@RequestMapping("/file")
@CrossOrigin()
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    private UploadGateway gateway;

    @PostMapping(value = "/upload")
    public ResponseEntity<ResponseDTO> uploadFile(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("type") String documentType,
                                                  @RequestParam("customerId") int customerId) {
        String message = "";
        try {
            FilesEntity fileDB = fileService.saveFile(file, documentType, customerId);
            message = FILE_UPLOAD_SUCCESS_MESSAGE + file.getOriginalFilename();
            File uploadFile = FileUtils.generateIndexFile(fileDB);
            gateway.uploadFile(uploadFile);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(message));
        } catch (Exception e) {
            System.out.println(e);
            message = FILE_UPLOAD_ERROR_MESSAGE + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(message));
        }
    }
}
