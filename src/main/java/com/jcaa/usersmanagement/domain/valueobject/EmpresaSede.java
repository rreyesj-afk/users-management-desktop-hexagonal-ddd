package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSedeException;
import java.util.Objects;

public record EmpresaSede(String nameSede, String descriptionSede) {

    private static final int MINIMUM_LENGTH = 3;

    public EmpresaSede {
        nameSede = Objects.requireNonNull(nameSede,"El nombre de la sede no puede ser nulo.").trim();
        descriptionSede = Objects.requireNonNull(descriptionSede, "La descripción de la sede no puede ser nula.").trim();
        validateNotEmpty(nameSede, descriptionSede);
        validateMinimumLength(nameSede);
    }

    private static void validateNotEmpty(final String nameSede, final String descriptionSede) {
        if (nameSede.isEmpty()) {throw InvalidEmpresaSedeException.becauseSedeNameIsEmpty();
        }
        if (descriptionSede.isEmpty()) {throw InvalidEmpresaSedeException.becauseSedeDescriptionIsEmpty();
        }
    }

    private static void validateMinimumLength(final String nameSede) {
        if (nameSede.length() < MINIMUM_LENGTH) {throw InvalidEmpresaSedeException.becauseLengthIsTooShort(MINIMUM_LENGTH);
        }
    }

}