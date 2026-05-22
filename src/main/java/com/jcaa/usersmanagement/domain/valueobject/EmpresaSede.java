package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSedeException;
import java.util.Objects;

public record EmpresaSede(String sedeName, String sedeDescription) {

    private static final int MINIMUM_LENGTH = 3;

    public EmpresaSede {
        sedeName = Objects.requireNonNull(sedeName,"El nombre de la sede no puede ser nulo.").trim();
        sedeDescription = Objects.requireNonNull(sedeDescription, "La descripción de la sede no puede ser nula.").trim();
        validateNotEmpty(sedeName, sedeDescription);
        validateMinimumLength(sedeName);
    }

    private static void validateNotEmpty(final String sedeName, final String descriptionSede) {
        if (sedeName.isEmpty()) {throw InvalidEmpresaSedeException.becauseSedeNameIsEmpty();
        }
        if (descriptionSede.isEmpty()) {throw InvalidEmpresaSedeException.becauseSedeDescriptionIsEmpty();
        }
    }

    private static void validateMinimumLength(final String nameSede) {
        if (nameSede.length() < MINIMUM_LENGTH) {throw InvalidEmpresaSedeException.becauseLengthIsTooShort(MINIMUM_LENGTH);
        }
    }

    @Override
    public String toString() {return sedeName + " - " + sedeDescription;}
}