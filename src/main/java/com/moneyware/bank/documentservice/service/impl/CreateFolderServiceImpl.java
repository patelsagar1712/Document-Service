package com.moneyware.bank.documentservice.service.impl;

import com.jcraft.jsch.ChannelSftp;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.integration.sftp.session.SftpSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CreateFolderServiceImpl {
    private final DefaultSftpSessionFactory sessionFactory;

    @PostConstruct
    public void createFolders() {
        createFolder("/", "/upload");
        createFolder("/upload", "/upload/done");
        createFolder("/upload", "/upload/topsecret");
        createFolder("/upload/topsecret", "/upload/topsecret/verysecret");
        createFolder("/upload/topsecret", "/upload/topsecret/lesssecret");
        createFolder("/upload", "/upload/trashcan");
    }

    @SneakyThrows
    private boolean doesFolderExist(String parent, String path) {
        SftpSession session = sessionFactory.getSession();
        ChannelSftp.LsEntry[] list = session.list(parent);
        for (int i = 0; i < list.length; i++) {
            String filename = list[i].getFilename();
            if (path.contains(filename))
                return true;
        }
        return false;
    }

    private void createFolder(String parent, String path) {
        try {
            if (doesFolderExist(parent, path)) {

                return;
            }
            sessionFactory.getSession().mkdir(path);

        } catch (IOException e) {

        }
    }
}
