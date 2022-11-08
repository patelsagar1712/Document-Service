package com.moneyware.bank.documentservice.upload;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import java.io.File;

import static com.moneyware.bank.documentservice.constants.MessageConstants.GATEWAY_CHANNEL;

@MessagingGateway
public interface UploadGateway {
    @Gateway(requestChannel = GATEWAY_CHANNEL)
    void uploadFile(File file);
}
