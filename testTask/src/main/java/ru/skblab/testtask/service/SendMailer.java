package ru.skblab.testtask.service;

import ru.skblab.testtask.dto.EmailAddress;
import ru.skblab.testtask.dto.EmailContent;

import java.util.concurrent.TimeoutException;

public interface SendMailer {
    void sendMail (EmailAddress toAddress, EmailContent messageBody) throws TimeoutException;
}
