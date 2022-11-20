package ru.skblab.testtask.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.skblab.testtask.exeption.UserVerificationNotFoundException;
import ru.skblab.testtask.jpa.entity.UserVerification;
import ru.skblab.testtask.jpa.repository.UserVerificationRepository;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
class UserVerificationServiceImplTest {

    @MockBean
    private UserVerificationRepository userVerificationRepository;

    @Autowired
    private UserVerificationServiceImpl userVerificationService;

    private final static UserVerification userVerification = new UserVerification();

    @Test
    void getEmptyUserVerificationTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.empty());
        Optional<UserVerification> userVerification = userVerificationService.getUserVerification(2L);
        assertTrue(userVerification.isEmpty());
    }

    @Test
    void getUserVerificationTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.of(userVerification));
        Optional<UserVerification> userVerification = userVerificationService.getUserVerification(2L);
        assertTrue(userVerification.isPresent());
    }

    @Test
    void setVerificationMessageStatusNotFoundTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.empty());
        UndeclaredThrowableException exception = assertThrows(UndeclaredThrowableException.class, () ->
                userVerificationService.setVerificationMessageStatus(1L, true));
        assertEquals(exception.getCause().getClass(), UserVerificationNotFoundException.class);
    }

    @Test
    void setVerificationMessageStatusTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.of(userVerification));
        userVerificationService.setVerificationMessageStatus(2L, true);
        Mockito.verify(userVerificationRepository, Mockito.times(1)).save(any());
    }



    @Test
    void setVerificationResultNotFoundTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.empty());
        UndeclaredThrowableException exception = assertThrows(UndeclaredThrowableException.class, () ->
                userVerificationService.setVerificationResult(1L, true));
        assertEquals(exception.getCause().getClass(), UserVerificationNotFoundException.class);
    }

    @Test
    void setVerificationResultTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.of(userVerification));
        userVerificationService.setVerificationResult(2L, true);
        Mockito.verify(userVerificationRepository, Mockito.times(1)).save(any());
    }

    @Test
    void setNotificationStatusNotFoundTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.empty());
        UndeclaredThrowableException exception = assertThrows(UndeclaredThrowableException.class, () ->
                userVerificationService.setNotificationStatus(1L, true));
        assertEquals(exception.getCause().getClass(), UserVerificationNotFoundException.class);
    }

    @Test
    void setNotificationStatusTest() {
        when(userVerificationRepository.findByUserId(any())).thenReturn(Optional.of(userVerification));
        userVerificationService.setNotificationStatus(2L, true);
        Mockito.verify(userVerificationRepository, Mockito.times(1)).save(any());
    }

    @Test
    void findUserWithUnsentNotificationTest() {
        when(userVerificationRepository.findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNull()).thenReturn(List.of(userVerification, userVerification));
        List<UserVerification> userWithUnsentNotifications = userVerificationService.findUserWithUnsentNotification();
        assertEquals(2, userWithUnsentNotifications.size());

    }

    @Test
    void findUserWithUnsentNotificationEmptyListTest() {
        when(userVerificationRepository.findAllByIsNotificationSendingIsFalseAndIsVerifiedNotNull()).thenReturn(List.of());
        List<UserVerification> userWithUnsentNotifications = userVerificationService.findUserWithUnsentNotification();
        assertEquals(0, userWithUnsentNotifications.size());
    }

    @Test
    void findUserWithUnsentVerificationMessageTest() {
        when(userVerificationRepository.findAllByIsVerificationMessageSendingIsFalse()).thenReturn(List.of(userVerification, userVerification));
        List<UserVerification> userWithUnsentNotifications = userVerificationService.findUserWithUnsentVerificationMessage();
        assertEquals(2, userWithUnsentNotifications.size());

    }

    @Test
    void findUserWithUnsentVerificationMessageEmptyListTest() {
        when(userVerificationRepository.findAllByIsVerificationMessageSendingIsFalse()).thenReturn(List.of());
        List<UserVerification> userWithUnsentNotifications = userVerificationService.findUserWithUnsentVerificationMessage();
        assertEquals(0, userWithUnsentNotifications.size());
    }
}