package ru.skblab.testtask.exeption;

public class UserVerificationNotFoundException extends Exception {
    public UserVerificationNotFoundException(Long id){
        super(String.format("User verification with user id: %s not found", id));
    }
}
