package com.jcaa.usersmanagement.domain.exception;

public final class EmpresaAlreadyExistsException extends RuntimeException {
    private static final String EMPRESA_EXISTS = "Ya existe una empresa con ese nombre.";

    private EmpresaAlreadyExistsException(final String message) {
        super(message);
    }

    public static EmpresaAlreadyExistsException becauseNameAlreadyExists(final String name) {
        return new EmpresaAlreadyExistsException(String.format(EMPRESA_EXISTS));
    }
}