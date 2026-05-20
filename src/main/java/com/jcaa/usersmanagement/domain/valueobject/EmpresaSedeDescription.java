package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSedeDescriptionException;
import java.util.Objects;

public record EmpresaSedeDescription(String value){

    public EmpresaSedeDescription {
        final String normalizedValue = Objects.requireNonNull(value, "La desccripción de la sede no puede estar vacía.");
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isBlank()){
            throw InvalidEmpresaSedeDescriptionException.becauseSedeDescriptionIsBlank();
        }
    }
}






