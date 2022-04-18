package com.deepbluec.auth_service.services;

public interface NotificationService {

    void sendNotification(String address, String subject, String content);

}
