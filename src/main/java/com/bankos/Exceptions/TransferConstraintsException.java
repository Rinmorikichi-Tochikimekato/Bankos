package com.bankos.Exceptions;

public class TransferConstraintsException extends RuntimeException{
    public TransferConstraintsException(String exceptionMessage){
        super(exceptionMessage);
    }
}
