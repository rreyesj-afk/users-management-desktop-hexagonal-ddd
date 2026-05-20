package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSedeNameException;

import java.util.Objects;

public record EmpresaSedeName(String value){

    public EmpresaSedeName {
        final String normalizedValue = Objects.requireNonNull(value, "El nombre de la sede no puede estar vacío." );
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isBlank()){
            throw InvalidEmpresaSedeNameException.becauseSedeNameIsBlank();
        }
    }
}






