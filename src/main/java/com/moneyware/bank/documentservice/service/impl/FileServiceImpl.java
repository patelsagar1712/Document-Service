package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.entity.FilesEntity;
import com.moneyware.bank.documentservice.exception.CustomException;
import com.moneyware.bank.documentservice.repository.FileRepository;
import com.moneyware.bank.documentservice.security.JwtUtil;
import com.moneyware.bank.documentservice.service.FileService;
import com.moneyware.bank.documentservice.upload.FileUploadConfig;
import com.moneyware.bank.documentservice.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

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

    private final FileRepository fileRepository;
    private final FileUploadConfig fileUploadConfig;
    private final JwtUtil jwtUtil;

    @Value("${app.file.upload-dir}")
    String fileStorageLocation;

    @Autowired
    public FileServiceImpl(Environment env, FileRepository fileRepository, FileUploadConfig fileUploadConfig, JwtUtil jwtUtil) {
        this.fileStorageLocationPath = Paths.get(env.getProperty("app.file.upload-dir", "./uploads/uFiles"))
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocationPath);
        } catch (Exception ex) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    COULD_NOT_CREATE_THE_DIRECTORY_WHERE_THE_UPLOADED_FILES_WILL_BE_STORED + ex.getMessage());
        }
        this.fileRepository = fileRepository;
        this.fileUploadConfig = fileUploadConfig;
        this.jwtUtil = jwtUtil;
    }

    @Scheduled(cron = "0 * * * * *") // 1 min
    public int upload() {
        AtomicInteger uploadedCount = new AtomicInteger();
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
                            uploadedCount.getAndIncrement();
                        } catch (IOException e) {
                            throw new CustomException(HttpStatus.NOT_FOUND, UPLOADED_FAILED);
                        }
                    } else {
                        log.info(UPLOADED_FAILED + document.getFileName());
                    }
                }
            }
        });
        return uploadedCount.get();
    }

    @Override
    public FilesEntity saveFile(MultipartFile file, String token) throws IOException {
        String fileName = file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocationPath.resolve(fileName);
        String customerId = jwtUtil.getUsernameFromToken(token.substring(7));
        try {
            long fileSize = Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            FilesEntity document = FilesEntity.builder()
                    .documentType(fileName.substring(fileName.lastIndexOf(".") + 1))
                    .fileName(fileName)
                    .status(COMPLETED_STATUS)
                    .timeStamp(LocalDate.now())
                    .fileSize(fileSize / 1024)
                    .customerId(customerId)
                    .build();
            log.info(UPLOADED_SUCCESSFULLY);
            FilesEntity filesEntity = fileRepository.save(document);
            uploadIndexFile(customerId, fileSize, filesEntity);
            return filesEntity;
        } catch (IOException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST,
                    UNABLE_TO_SAVE_DOCUMENT + e.getMessage());
        }
    }

    public void uploadIndexFile(String customerId, long fileSize, FilesEntity filesEntity) throws IOException {
        File uploadFile = FileUtils.generateIndexFile(filesEntity);

        FilesEntity indexFile = FilesEntity.builder()
                .documentType("txt")
                .fileName(uploadFile.getName())
                .status(COMPLETED_STATUS)
                .timeStamp(LocalDate.now())
                .fileSize(fileSize / 1024)
                .customerId(customerId)
                .build();

        FilesEntity indexFilesEntity1 = fileRepository.save(indexFile);
    }
}
