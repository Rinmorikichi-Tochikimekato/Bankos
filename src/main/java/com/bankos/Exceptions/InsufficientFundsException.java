package com.bankos.Exceptions;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(String exceptionMessage){
        super(exceptionMessage);
    }
}
