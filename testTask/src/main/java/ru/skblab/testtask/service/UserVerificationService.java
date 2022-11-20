package ru.skblab.testtask.service;

import ru.skblab.testtask.jpa.entity.UserVerification;

import java.util.List;
import java.util.Optional;

public interface UserVerificationService {
    void setVerificationMessageStatus(Long userId, Boolean status);

    Optional<UserVerification> getUserVerification(Long userId);

    void setVerificationResult(Long userId, Boolean isVerified);

    void setNotificationStatus(Long userId, Boolean status);

    List<UserVerification> findUserWithUnsentNotification();

    List<UserVerification> findUserWithUnsentVerificationMessage();
}

