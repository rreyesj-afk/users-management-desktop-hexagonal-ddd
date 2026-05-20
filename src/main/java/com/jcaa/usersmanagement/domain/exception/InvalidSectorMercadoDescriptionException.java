package com.jcaa.usersmanagement.domain.exception;

public final class InvalidSectorMercadoDescriptionException extends DomainException {

    private static final String MESSAGE_EMPTY = "La descripción no puede estar vacía.";

    private InvalidSectorMercadoDescriptionException(final String message) {super(message);}

    public static InvalidSectorMercadoDescriptionException becauseSectorDescriptionIsEmpty() {
        return new InvalidSectorMercadoDescriptionException(MESSAGE_EMPTY);
    }

}