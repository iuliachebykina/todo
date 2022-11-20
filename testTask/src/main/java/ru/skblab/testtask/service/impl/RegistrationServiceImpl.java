package ru.skblab.testtask.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.skblab.testtask.aop.annotation.Loggable;
import ru.skblab.testtask.dto.UserDto;
import ru.skblab.testtask.exeption.EmailExistException;
import ru.skblab.testtask.exeption.LoginExistException;
import ru.skblab.testtask.service.RegistrationService;
import ru.skblab.testtask.service.UserService;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationServiceImpl implements RegistrationService {
    final UserService userService;

    @Override
    @Loggable
    public void registerUser(UserDto userDto) throws LoginExistException, EmailExistException {
        if (userService.isExistLogin(userDto.getLogin())) {
            throw new LoginExistException(userDto.getLogin());
        }

        if (userService.isExistEmail(userDto.getEmail())) {
            throw new EmailExistException(userDto.getEmail());
        }
        userService.createUser(userDto);
    }


}
