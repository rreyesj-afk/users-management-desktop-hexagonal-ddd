package com.jcaa.usersmanagement.domain.exception;

public final class InvalidIncorporationDateException extends RuntimeException {

    private static final String MESSAGE_EMPTY = "IncorporationDate cannot be null.";
    private static final String MESSAGE_FUTURE = "IncorporationDate cannot be in the future.";

    private InvalidIncorporationDateException (final String message){super(message);}

    public static  InvalidIncorporationDateException becauseIsEmpty (){
        return new InvalidIncorporationDateException(MESSAGE_EMPTY);
    }

    public static  InvalidIncorporationDateException becauseIsFuture (){
        return new InvalidIncorporationDateException(MESSAGE_FUTURE);
    }

}