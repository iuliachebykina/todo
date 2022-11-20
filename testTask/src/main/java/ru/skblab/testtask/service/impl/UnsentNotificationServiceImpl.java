package ru.skblab.testtask.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.service.UnsentNotificationService;
import ru.skblab.testtask.service.UserVerificationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnsentNotificationServiceImpl implements UnsentNotificationService {
    final UserVerificationService userVerificationService;

    @Override
    @Loggable
    public List<Long> findAllUserIdWithUnsentNotification() {
        return userVerificationService.findUserWithUnsentNotification()
                .stream().map(v -> v.getUser().getId()).collect(Collectors.toList());
    }
}
