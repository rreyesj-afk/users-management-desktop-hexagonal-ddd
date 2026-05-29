package com.jcaa.usersmanagement.domain.exception;

public class InvalidPaisNameException extends RuntimeException {

    private static final String MESSAGE_INVALID = "PaisName '%s' is not valid.";

    private InvalidPaisNameException (final String message){super(message);}

    public static InvalidPaisNameException becauseValueIsInvalid(final String role) {
        return new InvalidPaisNameException(String.format(MESSAGE_INVALID, role));
    }
}
