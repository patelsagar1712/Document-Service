package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.entity.FilesEntity;
import com.moneyware.bank.documentservice.exception.CustomException;
import com.moneyware.bank.documentservice.repository.FileRepository;
import com.moneyware.bank.documentservice.service.FileService;
import com.moneyware.bank.documentservice.upload.FileUploadConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;

import static com.moneyware.bank.documentservice.constants.MessageConstants.COMPLETED_STATUS;
import static com.moneyware.bank.documentservice.constants.MessageConstants.COULD_NOT_CREATE_THE_DIRECTORY_WHERE_THE_UPLOADED_FILES_WILL_BE_STORED;
import static com.moneyware.bank.documentservice.constants.MessageConstants.CRON_JOB_STARTED_AT;
import static com.moneyware.bank.documentservice.constants.MessageConstants.UNABLE_TO_SAVE_DOCUMENT;
import static com.moneyware.bank.documentservice.constants.MessageConstants.UPLOADED_FAILED;
import static com.moneyware.bank.documentservice.constants.MessageConstants.UPLOADED_SUCCESSFULLY;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final Path fileStorageLocationPath;

    @Autowired
    FileRepository fileRepository;

    @Value("${app.file.upload-dir}")
    String fileStorageLocation;

    @Autowired
    FileUploadConfig fileUploadConfig;

    @Autowired
    public FileServiceImpl(Environment env) {
        this.fileStorageLocationPath = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/files"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocationPath);
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    COULD_NOT_CREATE_THE_DIRECTORY_WHERE_THE_UPLOADED_FILES_WILL_BE_STORED + ex.getMessage());


        }
    }

    @Scheduled(cron = "0 * * * * *") // 1 min
    public void upload() {
        fileRepository.findAll().forEach(document -> {
            log.info(CRON_JOB_STARTED_AT + Calendar.getInstance().getTime());
            if (document.getStatus().equals(COMPLETED_STATUS)) {
                String filePath = fileStorageLocation + "/" + document.getFileName();
                Path path = Paths.get(filePath);
                if (Files.exists(path)) {

                    boolean isUploaded = fileUploadConfig.uploadFile(filePath, "/uploads/files/" + document.getFileName());
                    if (isUploaded) {
                        log.info(UPLOADED_SUCCESSFULLY + document.getFileName());
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            throw new RuntimeException(HttpStatus.NOT_FOUND.toString());
                        }
                    } else {
                        log.info(UPLOADED_FAILED + document.getFileName());
                    }
                }
            }
        });
    }

    @Override
    public FilesEntity saveFile(MultipartFile file, String documentType, int customerId) throws IOException {
        String fileName = file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocationPath.resolve(fileName);
        try {
            long fileSize = Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            FilesEntity document = FilesEntity.builder()
                    .documentType(documentType)
                    .fileName(fileName)
                    .status(COMPLETED_STATUS)
                    .timeStamp(LocalDate.now())
                    .customerId(customerId)
                    .build();
            log.info(UPLOADED_SUCCESSFULLY);
            FilesEntity filesEntity = fileRepository.save(document);
            return filesEntity;
        } catch (IOException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    UNABLE_TO_SAVE_DOCUMENT + e.getMessage());
        }
    }
}
