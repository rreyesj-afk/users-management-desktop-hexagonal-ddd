package com.jcaa.usersmanagement.domain.exception;

public final class InvalidEmpresaAnnualBillingException extends DomainException {

    private static final String MESSAGE_ANNUALBILLING_IS_NULL = "La facturación anual no puede estar vacía." ;
    private static final String MESSAGE_ANNUALBILLING_NEGATIVE = "La facturación anual no puede ser negativa." ;

    private InvalidEmpresaAnnualBillingException(final String message){super(message);}

    public static InvalidEmpresaAnnualBillingException becauseIsNull() {
        return new InvalidEmpresaAnnualBillingException(MESSAGE_ANNUALBILLING_IS_NULL);
    }

    public static InvalidEmpresaAnnualBillingException becauseIsNegative() {
        return new InvalidEmpresaAnnualBillingException(MESSAGE_ANNUALBILLING_NEGATIVE);
    }
}