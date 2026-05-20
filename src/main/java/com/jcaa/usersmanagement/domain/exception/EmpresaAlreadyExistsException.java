package com.jcaa.usersmanagement.domain.exception;

public final class EmpresaAlreadyExistsException extends RuntimeException {
    private static final String EMPRESA_EXISTS = "Ya existe una empresa con el nombre '%d'.";

    private EmpresaAlreadyExistsException(final String message) {
        super(message);
    }

    public static EmpresaAlreadyExistsException becauseNameAlreadyExists(final String empresaName) {
        return new EmpresaAlreadyExistsException(String.format(EMPRESA_EXISTS, empresaName));
    }
}