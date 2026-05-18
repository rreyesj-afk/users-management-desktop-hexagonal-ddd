package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaNameException extends RuntimeException {
    private static final String MESSAGE_EMPTY = "El nombre de la empresa no puede estar vacío";
    private static final String MESSAGE_TOO_SHORT = "El nombre de la empresa debe tener al menos %d carácteres.";

    private InvalidEmpresaNameException(final String message) {
        super(message);
    }

    public static InvalidEmpresaNameException becauseValueIsEmpty() {
        return new InvalidEmpresaNameException(MESSAGE_EMPTY);
    }

    public static InvalidEmpresaNameException becauseLengthIsTooShort(final int minimumLength) {
        return new InvalidEmpresaNameException(String.format(MESSAGE_TOO_SHORT, minimumLength));
    }
}
