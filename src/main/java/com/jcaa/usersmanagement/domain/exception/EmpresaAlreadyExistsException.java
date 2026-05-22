package com.jcaa.usersmanagement.domain.exception;

public final class EmpresaAlreadyExistsException extends DomainException {
    private static final String EMPRESA_EXISTS = "Ya existe una empresa con el nombre '%s'.";

    private EmpresaAlreadyExistsException(final String message) {
        super(message);
    }

    public static EmpresaAlreadyExistsException becauseNameAlreadyExists(final String empresaName) {
        return new EmpresaAlreadyExistsException(String.format(EMPRESA_EXISTS, empresaName));
    }
}