package main.java.com.bankos.Exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String exceptionMessage){
        super(exceptionMessage);
    }
}
