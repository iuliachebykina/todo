package ru.skblab.testtask.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.common.IdType;
import ru.skblab.testtask.dto.NameDto;
import ru.skblab.testtask.dto.UserVerifiedAnswerMessage;
import ru.skblab.testtask.dto.UserVerifiedRequestMessage;
import ru.skblab.testtask.exeption.UserNotFoundException;
import ru.skblab.testtask.jpa.entity.User;
import ru.skblab.testtask.service.*;

import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationServiceImpl implements VerificationService {
    final MessagingService messagingService;
    final NotificationService notificationService;
    final UserVerificationService userVerificationService;
    final UserService userService;

    @SneakyThrows
    @Override
    @Loggable
    public void verifyUser(Long userId) {
        Optional<User> user = userService.getUser(userId);
        if(user.isEmpty()){
            throw new UserNotFoundException(IdType.ID, userId.toString());
        }
        NameDto name = NameDto.builder()
                .firstName(user.get().getName().getFirstName())
                .lastName(user.get().getName().getLastName())
                .patronymic(user.get().getName().getPatronymic())
                .build();
        UserVerifiedRequestMessage message = UserVerifiedRequestMessage.builder()
                .email(user.get().getEmail())
                .name(name)
                .build();

        UserVerifiedAnswerMessage answer;
        try {
            answer = messagingService.doRequest(message);
        } catch (TimeoutException e){
            userVerificationService.setVerificationMessageStatus(userId, false);
            return;
        }
        userVerificationService.setVerificationMessageStatus(userId, true);
        userVerificationService.setVerificationResult(userId, answer.getIsVerified());

        notificationService.notifyUserAboutVerification(userId);

    }


}
