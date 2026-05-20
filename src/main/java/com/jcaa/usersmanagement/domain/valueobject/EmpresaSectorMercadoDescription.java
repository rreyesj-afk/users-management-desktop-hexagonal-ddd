package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidSectorMercadoDescriptionException;
import java.util.Objects;

public record EmpresaSectorMercadoDescription(String value) {

    public EmpresaSectorMercadoDescription {
        final String normalizedValue = Objects.requireNonNull(value, "La descripción no puede estar vacía.").trim();
        validateSectorDescription(normalizedValue);
        value = normalizedValue;
    }

    private static void validateSectorDescription(final String normalizedValue) {
        if (normalizedValue.isEmpty()) {
            throw InvalidSectorMercadoDescriptionException.becauseSectorDescriptionIsEmpty();
        }
    }

    @Override
    public String toString() {return value;}
}