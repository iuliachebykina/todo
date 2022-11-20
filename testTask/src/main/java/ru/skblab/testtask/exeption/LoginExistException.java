package ru.skblab.testtask.exeption;

public class LoginExistException extends Exception{
    public LoginExistException(String login){
        super(String.format("User with login: %s already exist", login));
    }
}
