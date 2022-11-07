package com.moneyware.bank.documentservice.upload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.sftp.outbound.SftpMessageHandler;
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory;
import org.springframework.messaging.MessageHandler;

import java.time.LocalDateTime;

import static com.moneyware.bank.documentservice.constants.MessageConstants.GATEWAY_CHANNEL;

@Configuration
public class FileUploadConfig {
    @Value("${sftp.host}")
    private String host;

    @Value("${sftp.port}")
    private int port;

    @Value("${sftp.user}")
    private String user;

    @Value("${sftp.password}")
    private String password;


    @Bean
    public DefaultSftpSessionFactory gimmeFactory() {
        DefaultSftpSessionFactory factory = new DefaultSftpSessionFactory();
        factory.setHost("192.168.0.107");
        factory.setPort(port);
        factory.setAllowUnknownKeys(true);
        factory.setUser(user);
        factory.setPassword(password);
        return factory;
    }

    @Bean
    @ServiceActivator(inputChannel = GATEWAY_CHANNEL)
    MessageHandler uploadHandler(DefaultSftpSessionFactory factory) {
        SftpMessageHandler messageHandler = new SftpMessageHandler(gimmeFactory());
        messageHandler.setRemoteDirectoryExpression(new LiteralExpression("F:/Document-service/Temp"));
        messageHandler.setFileNameGenerator(message -> String.format("mytextfile_%s.txt", LocalDateTime.now()));
        return messageHandler;
    }
}
