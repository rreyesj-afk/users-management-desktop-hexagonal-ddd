package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaIdException extends DomainException {

    private static final String MESSAGE_EMPTY = "La Id de la empresa no puede estar vacía.";

    private InvalidEmpresaIdException(final String message) {
        super(message);
    }

    public static InvalidEmpresaIdException becauseValueIsEmpty() {
        return new InvalidEmpresaIdException(MESSAGE_EMPTY);
    }
}
