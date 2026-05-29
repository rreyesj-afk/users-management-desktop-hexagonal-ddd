package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaAnnualBillingException;
import java.math.BigDecimal;
import java.util.Objects;

public record EmpresaAnnualBilling(BigDecimal value) {

    public EmpresaAnnualBilling {
        final BigDecimal normalizedValue = Objects.requireNonNull(value, "AnnualBilling cannot be null");
        validateNotNegative(normalizedValue);
    }

    private static void validateNotNegative(final BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw InvalidEmpresaAnnualBillingException.becauseIsNegative();
        }
    }

    @Override
    public String toString() {
        return value.toPlainString();
    }
}
