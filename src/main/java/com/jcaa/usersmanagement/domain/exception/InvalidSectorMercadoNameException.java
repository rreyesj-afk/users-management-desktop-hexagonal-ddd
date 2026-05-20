package com.jcaa.usersmanagement.domain.exception;

public final class InvalidSectorMercadoNameException extends DomainException {

    private static final String MESSAGE_EMPTY = "El nombre no puede estar vacío.";

    private InvalidSectorMercadoNameException(final String message) {super(message);}

    public static InvalidSectorMercadoNameException becauseSectorNameIsEmpty() {
        return new InvalidSectorMercadoNameException(MESSAGE_EMPTY);
    }

}