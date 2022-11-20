package ru.skblab.testtask.exeption;

import ru.skblab.testtask.common.IdType;

import java.util.Locale;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(IdType idType, String email){
        super(String.format("User with %s: %s not found", idType.name().toLowerCase(Locale.ROOT), email));
    }

}
