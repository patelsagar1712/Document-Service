package com.moneyware.bank.documentservice.service;

import com.moneyware.bank.documentservice.entity.FilesEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    FilesEntity saveFile(MultipartFile file, String token) throws IOException;
}
