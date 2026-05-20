package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSectorMercadoException;
import java.util.Objects;

public record EmpresaSectorMercado(String nameSector, String descriptionSector) {

    private static final int MINIMUM_LENGTH = 3;

    public EmpresaSectorMercado{
        nameSector = Objects.requireNonNull(nameSector,"El nombre del sector no puede ser nulo.").trim();
        descriptionSector = Objects.requireNonNull(descriptionSector, "La descripción del sector no puede ser nula.").trim();
        validateNotEmpty(nameSector, descriptionSector);
        validateMinimumLength(nameSector);
    }

    private static void validateNotEmpty(final String nameSector, final String descriptionSector) {
        if (nameSector.isEmpty()) {throw InvalidEmpresaSectorMercadoException.becauseSectorNameIsEmpty();
        }
        if (descriptionSector.isEmpty()) {throw InvalidEmpresaSectorMercadoException.becauseSectorDescriptionIsEmpty();
        }
    }

    private static void validateMinimumLength(final String nameSector) {
        if (nameSector.length() < MINIMUM_LENGTH) {throw InvalidEmpresaSectorMercadoException.becauseLengthIsTooShort(MINIMUM_LENGTH);
        }
    }

}