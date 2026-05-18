package com.jcaa.usersmanagement.domain.enums;

import com.jcaa.usersmanagement.domain.exception.InvalidPaisNameException;

public enum PaisName {
    COLOMBIA,
    ECUADOR,
    PANAMA,
    MEXICO,
    ARGENTINA;

    public static PaisName fromString(final String value) {
        for (final PaisName status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw InvalidPaisNameException.becauseValueIsInvalid(value);
    }

}
