package ru.skblab.testtask.service;

import ru.skblab.testtask.dto.UserDto;
import ru.skblab.testtask.jpa.entity.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUser(Long id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByLogin(String login);

    User createUser(UserDto user);

    Boolean isExistEmail(String email);

    Boolean isExistLogin(String login);
}
