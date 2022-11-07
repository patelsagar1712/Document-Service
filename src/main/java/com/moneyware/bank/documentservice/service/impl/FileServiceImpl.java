package com.moneyware.bank.documentservice.service.impl;

import com.moneyware.bank.documentservice.entity.FilesEntity;
import com.moneyware.bank.documentservice.repository.FileRepository;
import com.moneyware.bank.documentservice.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.moneyware.bank.documentservice.constants.MessageConstants.COMPLETED_STATUS;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    FileRepository fileRepository;

    @Override
    public FilesEntity saveFile(MultipartFile file, String documentType, int customerId) throws IOException {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        FilesEntity fileDB = new FilesEntity(fileName, file.getSize(), new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
                customerId, documentType, COMPLETED_STATUS, file.getBytes());

        return fileRepository.save(fileDB);
    }
}
