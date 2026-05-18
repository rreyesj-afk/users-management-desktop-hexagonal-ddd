package com.jcaa.usersmanagement.domain.exception;

public final class EmpresaNotFoundException extends DomainException {

    public static final String MESSAGE_EMPRESA_NOT_FOUND = "Empresa not found with id";

    private EmpresaNotFoundException (final String message){super(message);}

    public static EmpresaNotFoundException becauseIdWasNotFound (final String EmpresaId) {
        return new EmpresaNotFoundException(String.format(MESSAGE_EMPRESA_NOT_FOUND, EmpresaId));
    }


}