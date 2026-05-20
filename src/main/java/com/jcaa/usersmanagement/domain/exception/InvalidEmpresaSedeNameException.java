package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaSedeNameException extends DomainException {

    public static final String MESSAGE_EMPTY = "El nombre de la sede no puede estar vacío.";

    public InvalidEmpresaSedeNameException(final String message) {super(message);}

    public static InvalidEmpresaSedeNameException becauseSedeNameIsBlank() {
        return new InvalidEmpresaSedeNameException(MESSAGE_EMPTY);
    }

}
