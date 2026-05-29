package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaSedeException extends DomainException {

    private static final String MESSAGE_NAME_EMPTY = "El nombre no puede estar vacío.";
    private static final String MESSAGE_DESCRIPTION_EMPTY = "La descripción no puede estar vacía";
    private static final String MESSAGE_TOO_SHORT = "El nombre debe tener al menos '%d' caracteres. ";

    private InvalidEmpresaSedeException(final String message) {super(message);}

    public static InvalidEmpresaSedeException becauseSedeNameIsEmpty() {
        return new InvalidEmpresaSedeException(MESSAGE_NAME_EMPTY);
    }

    public static InvalidEmpresaSedeException becauseSedeDescriptionIsEmpty() {
        return new InvalidEmpresaSedeException(MESSAGE_DESCRIPTION_EMPTY);
    }

    public static InvalidEmpresaSedeException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidEmpresaSedeException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }

}