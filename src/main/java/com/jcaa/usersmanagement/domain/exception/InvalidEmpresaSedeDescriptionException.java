package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaSedeDescriptionException extends DomainException {

    public static final String MESSAGE_EMPTY = "La desccripción no puede estar vacía.";

    public InvalidEmpresaSedeDescriptionException(final String message) {super(message);}

    public static InvalidEmpresaSedeDescriptionException becauseSedeDescriptionIsBlank() {
        return new InvalidEmpresaSedeDescriptionException(MESSAGE_EMPTY);
    }

}
