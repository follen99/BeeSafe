package it.ranauro.beesafe.backend.utils.exceptions;

public class NotAuthorizatedException extends Exception{
    public NotAuthorizatedException(String message) {
        super(message);
    }
}
