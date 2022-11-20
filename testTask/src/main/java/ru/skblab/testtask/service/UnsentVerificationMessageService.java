package ru.skblab.testtask.service;

import java.util.List;

public interface UnsentVerificationMessageService {
    List<Long> findAllUserIdWithUnsentVerificationMessage();
}

