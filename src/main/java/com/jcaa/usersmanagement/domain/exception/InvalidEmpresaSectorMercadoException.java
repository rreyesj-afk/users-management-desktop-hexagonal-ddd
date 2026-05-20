package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaSectorMercadoException extends DomainException {

    private static final String MESSAGE_NAME_EMPTY = "El nombre no puede estar vacío.";
    private static final String MESSAGE_DESCRIPTION_EMPTY = "La descripción no puede estar vacía";
    private static final String MESSAGE_TOO_SHORT = "El nombre debe tener al menos '%d' caracteres. ";

    private InvalidEmpresaSectorMercadoException(final String message) {super(message);}

    public static InvalidEmpresaSectorMercadoException becauseSectorNameIsEmpty() {
        return new InvalidEmpresaSectorMercadoException(MESSAGE_NAME_EMPTY);
    }

    public static InvalidEmpresaSectorMercadoException becauseSectorDescriptionIsEmpty() {
        return new InvalidEmpresaSectorMercadoException(MESSAGE_DESCRIPTION_EMPTY);
    }

    public static InvalidEmpresaSectorMercadoException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidEmpresaSectorMercadoException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }

}