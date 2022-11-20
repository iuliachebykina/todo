package ru.skblab.testtask.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.repository.UserVerificationRepository;
import ru.skblab.testtask.service.UserVerificationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVerificationServiceImpl implements UserVerificationService {
    final UserVerificationRepository userVerificationRepository;
    @SneakyThrows
    @Override
    public void setVerificationMessageStatus(Long userId, Boolean status) {
        Optional<UserVerification> userVerificationOpt = getUserVerification(userId);
        if(userVerificationOpt.isEmpty()){
            return;
        }
        UserVerification userVerification = userVerificationOpt.get();
        userVerification.setIsVerificationMessageSending(status);
        userVerificationRepository.save(userVerification);
    }

    @Override
    public Optional<UserVerification> getUserVerification(Long userId) {
        return userVerificationRepository.findByUserId(userId);
    }

    @SneakyThrows
    @Override
    public void setVerificationResult(Long userId, Boolean isVerified) {
        Optional<UserVerification> userVerificationOpt = getUserVerification(userId);
        if(userVerificationOpt.isEmpty()){
            return;
        }
        UserVerification userVerification = userVerificationOpt.get();

        userVerification.setIsVerified(isVerified);
        userVerificationRepository.save(userVerification);

    }

    @SneakyThrows
    @Override
    public void setNotificationStatus(Long userId, Boolean status) {
        Optional<UserVerification> userVerificationOpt = getUserVerification(userId);
        if(userVerificationOpt.isEmpty()){
            return;
        }
        UserVerification userVerification = userVerificationOpt.get();
        userVerification.setIsNotificationSending(status);
        userVerificationRepository.save(userVerification);

    }

    @Override
    public List<UserVerification> findUserWithUnsentNotification() {
        return userVerificationRepository.findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNull();
    }

    @Override
    public List<UserVerification> findUserWithUnsentVerificationMessage() {
        return userVerificationRepository.findAllByIsVerificationMessageSendingIsFalse();
    }


}
