package ru.skblab.testtask.service;

import ru.skblab.testtask.dto.UserDto;
import ru.skblab.testtask.exeption.EmailExistException;
import ru.skblab.testtask.exeption.LoginExistException;

import java.util.concurrent.TimeoutException;

public interface RegistrationService {
    void registerUser(UserDto userDto) throws LoginExistException, EmailExistException, TimeoutException;
}
