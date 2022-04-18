package com.zantabri.auth_service.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class EmailNotificationService implements NotificationService{

    @Value("${jwt.signing.key}")
    private String signingKey;

    //TODO implement send email notification service
    @Override
    public void sendNotification(String address, String subject, String content) {

    }

}
