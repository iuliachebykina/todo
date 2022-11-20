package ru.skblab.testtask.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.exeption.UserVerificationNotFoundException;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.repository.UserVerificationRepository;
import ru.skblab.testtask.service.UserVerificationService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserVerificationServiceImpl implements UserVerificationService {
    final UserVerificationRepository userVerificationRepository;

    @SneakyThrows
    @Override
    @Loggable
    @Transactional
    public void setVerificationMessageStatus(Long userId, Boolean status) {
        Optional<UserVerification> userVerificationOpt = getUserVerification(userId);
        if(userVerificationOpt.isEmpty()){
            throw new UserVerificationNotFoundException(userId);
        }
        UserVerification userVerification = userVerificationOpt.get();
        userVerification.setIsVerificationMessageSending(status);
        userVerificationRepository.save(userVerification);
    }

    @Override
    @Loggable
    public Optional<UserVerification> getUserVerification(Long userId) {
        return userVerificationRepository.findByUserId(userId);
    }

    @SneakyThrows
    @Override
    @Loggable
    @Transactional
    public void setVerificationResult(Long userId, Boolean isVerified) {
        Optional<UserVerification> userVerificationOpt = getUserVerification(userId);
        if(userVerificationOpt.isEmpty()){
            throw new UserVerificationNotFoundException(userId);
        }
        UserVerification userVerification = userVerificationOpt.get();

        userVerification.setIsVerified(isVerified);
        userVerificationRepository.save(userVerification);

    }

    @SneakyThrows
    @Override
    @Loggable
    @Transactional
    public void setNotificationStatus(Long userId, Boolean status) {
        Optional<UserVerification> userVerificationOpt = getUserVerification(userId);
        if(userVerificationOpt.isEmpty()){
            throw new UserVerificationNotFoundException(userId);

        }
        UserVerification userVerification = userVerificationOpt.get();
        userVerification.setIsNotificationSending(status);
        userVerificationRepository.save(userVerification);

    }

    @Override
    @Loggable
    public List<UserVerification> findUserWithUnsentNotification() {
        return userVerificationRepository.findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNull();
    }

    @Override
    @Loggable
    public List<UserVerification> findUserWithUnsentVerificationMessage() {
        return userVerificationRepository.findAllByIsVerificationMessageSendingIsFalse();
    }


}
