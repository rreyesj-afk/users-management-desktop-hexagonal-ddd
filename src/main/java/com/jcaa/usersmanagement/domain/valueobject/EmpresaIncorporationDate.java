package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidIncorporationDateException;
import java.time.LocalDate;

public record EmpresaIncorporationDate(LocalDate value) {

    public EmpresaIncorporationDate {validateNotNull(value);validateIsFuture(value);}

    private static void validateNotNull(final LocalDate value) {
        if (value == null) {
            throw InvalidIncorporationDateException.becauseIsEmpty();
        }
    }

    private static void validateIsFuture(final LocalDate value) {
        if (value.isAfter(LocalDate.now())) {
            throw InvalidIncorporationDateException.becauseIsFuture();
        }
    }

    @Override
    public String toString() {return value.toString();}
}