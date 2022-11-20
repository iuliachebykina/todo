package ru.skblab.testtask.exeption;

public class EmailExistException extends Exception{
    public EmailExistException(String email){
        super(String.format("User with email: %s already exist", email));
    }
}
