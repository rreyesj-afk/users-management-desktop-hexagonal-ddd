package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaIdException;
import java.util.Objects;

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

    public static EmpresaId fromExistingValue(final String value) {return new EmpresaId(value);}

    @Override
    public String toString() {
        return value;
    }

}
