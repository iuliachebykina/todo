package ru.skblab.testtask.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skblab.testtask.jpa.entity.UserVerification;

import java.util.List;
import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long> {
    Optional<UserVerification> findByUserId(Long userId);

    List<UserVerification> findAllByIsVerificationMessageSendingIsFalse();

    List<UserVerification> findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNull();
}
