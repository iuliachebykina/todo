package ru.skblab.testtask.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skblab.testtask.dto.*;
import ru.skblab.testtask.exeption.UserNotFoundException;
import ru.skblab.testtask.jpa.entity.User;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.entity.valueType.Name;
import ru.skblab.testtask.service.MessagingService;
import ru.skblab.testtask.service.NotificationService;
import ru.skblab.testtask.service.UserService;
import ru.skblab.testtask.service.UserVerificationService;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class VerificationServiceImplTest {

    @MockBean
    private MessagingService messagingService;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private UserVerificationService userVerificationService;
    @MockBean
    private UserService userService;

    @Autowired
    VerificationServiceImpl verificationService;

    private final static UserVerifiedAnswerMessage unsuccessfullyAnswer = UserVerifiedAnswerMessage.builder()
            .isVerified(false)
            .build();

    private final static UserVerifiedAnswerMessage successfullyAnswer = UserVerifiedAnswerMessage.builder()
            .isVerified(true)
            .build();

    private final static UserVerification userVerification = new UserVerification();


    private final static NameDto name = NameDto.builder()
            .firstName("Юлия")
            .lastName("Чебыкина")
            .patronymic("Владимировна")
            .build();

    private final static String email = "iulia@gmail.ru";

    private final static String login = "iulia";
    private final static String password = "qwerty";


    private final static User user = new User(2L,
            login,
            email,
            password,
            new Name(name.getFirstName(), name.getLastName(), name.getLastName()),
            userVerification,
            false);

    @Test
    public void userNotFoundException(){
        when(userService.getUser(1L)).thenReturn(Optional.empty());
        UndeclaredThrowableException exception = assertThrows(UndeclaredThrowableException.class, () -> verificationService.verifyUser(1L));
        assertEquals(exception.getCause().getClass(), UserNotFoundException.class);

    }

    @Test
    public void notifyUserWithThrowTest() throws TimeoutException {
        Long userId = 2L;

        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        Mockito.doThrow(new TimeoutException("Timeout")).when(messagingService).doRequest(any());
        verificationService.verifyUser(userId);

        Mockito.verify(userVerificationService, Mockito.times(1)).setVerificationMessageStatus(userId, false);
        Mockito.verify(userVerificationService, Mockito.times(0)).setVerificationMessageStatus(userId, true);

        Mockito.verify(userVerificationService, Mockito.times(0)).setVerificationResult(eq(userId), any());
        Mockito.verify(notificationService, Mockito.times(0)).notifyUserAboutVerification(userId);


    }

    @Test
    public void notifyUserWithoutThrowTest() throws TimeoutException {
        Long userId = 2L;
        when(messagingService.doRequest(any())).thenReturn(unsuccessfullyAnswer);

        when(userService.getUser(userId)).thenReturn(Optional.of(user));
        verificationService.verifyUser(userId);

        Mockito.verify(userVerificationService, Mockito.times(0)).setVerificationMessageStatus(userId, false);
        Mockito.verify(userVerificationService, Mockito.times(1)).setVerificationMessageStatus(userId, true);
        Mockito.verify(userVerificationService, Mockito.times(1)).setVerificationResult(eq(userId), any());
        Mockito.verify(notificationService, Mockito.times(1)).notifyUserAboutVerification(userId);


    }

    @Test
    public void unsuccessfullyVerifiedTest() throws TimeoutException {
        Long userId = 2L;

        when(messagingService.doRequest(any())).thenReturn(unsuccessfullyAnswer);
        when(userService.getUser(userId)).thenReturn(Optional.of(user));

        verificationService.verifyUser(userId);
        Mockito.verify(userVerificationService, Mockito.times(1)).setVerificationResult(userId, false);


    }

    @Test
    public void successfullyVerifiedTest() throws TimeoutException {
        Long userId = 2L;

        when(messagingService.doRequest(any())).thenReturn(successfullyAnswer);
        when(userService.getUser(userId)).thenReturn(Optional.of(user));

        verificationService.verifyUser(userId);
        Mockito.verify(userVerificationService, Mockito.times(1)).setVerificationResult(userId, true);
    }


}