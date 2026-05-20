package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidSectorMercadoNameException;

import java.util.Objects;

public record EmpresaSectorMercadoName(String value) {

    public EmpresaSectorMercadoName {
        final String normalizedValue = Objects.requireNonNull(value, "El nombre no puede estar vacío.");
        validateNotEmpty(normalizedValue);
        value = normalizedValue;
    }

    private static void validateNotEmpty(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidSectorMercadoNameException.becauseSectorNameIsEmpty();
        }
    }


}
