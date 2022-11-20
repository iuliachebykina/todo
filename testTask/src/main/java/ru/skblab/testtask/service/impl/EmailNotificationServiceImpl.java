package ru.skblab.testtask.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.common.IdType;
import ru.skblab.testtask.dto.EmailAddress;
import ru.skblab.testtask.dto.EmailContent;
import ru.skblab.testtask.dto.NameDto;
import ru.skblab.testtask.exeption.UserNotFoundException;
import ru.skblab.testtask.exeption.UserVerificationNotFoundException;
import ru.skblab.testtask.jpa.entity.User;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.service.NotificationService;
import ru.skblab.testtask.service.SendMailer;
import ru.skblab.testtask.service.UserService;
import ru.skblab.testtask.service.UserVerificationService;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service("emailNotificationServiceImpl")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmailNotificationServiceImpl implements NotificationService {
    final SendMailer sendMailer;
    final UserService userService;
    final UserVerificationService userVerificationService;

    @Value("${app.user.verification.email.message.successfully}")
    String successfullyEmail;
    @Value("${app.user.verification.email.subject}")
    String emailSubject;
    @Value("${app.user.verification.email.message.unsuccessfully}")
    String unsuccessfullyEmail;

    @SneakyThrows
    @Override
    @Loggable
    public void notifyUserAboutVerification(Long userId) {
        Optional<User> user = userService.getUser(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(IdType.ID, userId.toString());
        }
        EmailAddress emailAddress = EmailAddress.builder()
                .to(user.get().getEmail())
                .build();
        NameDto name = NameDto.builder()
                .firstName(user.get().getName().getFirstName())
                .lastName(user.get().getName().getLastName())
                .patronymic(user.get().getName().getPatronymic())
                .build();
        Optional<UserVerification> userVerification = userVerificationService.getUserVerification(userId);
        if(userVerification.isEmpty()){
            throw new UserVerificationNotFoundException(userId);
        }
        EmailContent content;
        if (userVerification.get().getIsVerified()) {
            content = EmailContent.builder()
                    .message(String.format(successfullyEmail, name.getLastName(), name.getFirstName(), name.getPatronymic()))
                    .subject(emailSubject)
                    .build();

        } else {
            content = EmailContent.builder()
                    .message(String.format(unsuccessfullyEmail, name.getLastName(), name.getFirstName(), name.getPatronymic()))
                    .subject(emailSubject)
                    .build();

        }

        try {
            sendMailer.sendMail(emailAddress, content);
            userVerificationService.setNotificationStatus(userId, true);

        } catch (TimeoutException e) {
            userVerificationService.setNotificationStatus(userId, false);
        }
    }
}
