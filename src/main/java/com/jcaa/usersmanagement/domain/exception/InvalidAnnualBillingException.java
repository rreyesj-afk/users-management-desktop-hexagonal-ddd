package com.jcaa.usersmanagement.domain.exception;

public final class InvalidAnnualBillingException extends DomainException {

    public static final String MESSAGE_ANNUALBILLING_IS_NULL = "La facturación anual no puede estar vacía." ;
    public static final String MESSAGE_ANNUALBILLING_NEGATIVE = "La facturación anual no puede ser negativa." ;

    private InvalidAnnualBillingException (final String message){super(message);}

    public static InvalidAnnualBillingException becauseIsNull() {
        return new InvalidAnnualBillingException(MESSAGE_ANNUALBILLING_IS_NULL);
    }

    public static InvalidAnnualBillingException becauseIsNegative() {
        return new InvalidAnnualBillingException(MESSAGE_ANNUALBILLING_NEGATIVE);
    }
}