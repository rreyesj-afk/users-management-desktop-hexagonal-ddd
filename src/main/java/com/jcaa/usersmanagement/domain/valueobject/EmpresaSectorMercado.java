package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSectorMercadoException;
import java.util.Objects;

public record EmpresaSectorMercado(String sectorName, String sectorDescription) {

    private static final int MINIMUM_LENGTH = 3;

    public EmpresaSectorMercado{
        sectorName = Objects.requireNonNull(sectorName,"El nombre del sector no puede ser nulo.").trim();
        sectorDescription = Objects.requireNonNull(sectorDescription, "La descripción del sector no puede ser nula.").trim();
        validateNotEmpty(sectorName, sectorDescription);
        validateMinimumLength(sectorName);
    }

    private static void validateNotEmpty(final String sectorName, final String sectorDescription) {
        if (sectorName.isEmpty()) {throw InvalidEmpresaSectorMercadoException.becauseSectorNameIsEmpty();
        }
        if (sectorDescription.isEmpty()) {throw InvalidEmpresaSectorMercadoException.becauseSectorDescriptionIsEmpty();
        }
    }

    private static void validateMinimumLength(final String sectorName) {
        if (sectorName.length() < MINIMUM_LENGTH) {throw InvalidEmpresaSectorMercadoException.becauseLengthIsTooShort(MINIMUM_LENGTH);
        }
    }

    @Override
    public String toString() {return sectorName + " - " + sectorDescription;}

}