package ru.skblab.testtask.service;

import java.util.List;

public interface UnsentNotificationService {
    List<Long> findAllUserIdWithUnsentNotification();
}

