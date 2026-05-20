package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaIdException;
import java.util.Objects;
import java.util.UUID;

public record EmpresaId (String value) {

    public EmpresaId {
        final String normalizedValue = Objects.requireNonNull(value, "EmpresaId can't be empty.").trim();
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidEmpresaIdException.becauseValueIsEmpty();
        }
    }

    //Genera un ID nuevo y único cada vez que se llama
    public static EmpresaId newId() {return new EmpresaId(UUID.randomUUID().toString());}

    public static EmpresaId fromExistingValue(String valor) {return new EmpresaId(valor);}

    @Override
    public String toString() {
        return value;
    }

}
