package com.jcaa.usersmanagement.domain.exception;

public final class EmpresaNotFoundException extends DomainException {

    private static final String MESSAGE_EMPRESA_NOT_FOUND = "Empresa not found with id '%s'";

    private EmpresaNotFoundException (final String message){super(message);}

    public static EmpresaNotFoundException becauseIdWasNotFound (final String empresaId) {
        return new EmpresaNotFoundException(String.format(MESSAGE_EMPRESA_NOT_FOUND, empresaId));
    }


}