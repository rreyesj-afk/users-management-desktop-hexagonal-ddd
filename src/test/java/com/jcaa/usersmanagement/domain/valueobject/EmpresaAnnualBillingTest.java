package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaAnnualBillingException;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EmpresaAnnualBillingTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @DisplayName("Verificar que el valor de la facturación anual sea válido")
    @ValueSource(strings = {"0", "1000", "15000.50", "9999999.99"})
    void shouldCreateEmpresaAnnualBillingWithValidValue(final String input) {
        // Arrange
        final BigDecimal expectedValue = new BigDecimal(input);
        // Act
        final EmpresaAnnualBilling annualBilling = new EmpresaAnnualBilling(new BigDecimal(input));
        // Assert
        assertEquals(expectedValue, annualBilling.value());
    }

    @Test
    @DisplayName("Verifica que el valor de la facturación anual no sea nulo")
    void shouldThrowNullPointerExceptionWhenAnnualBillingIsNull() {
        assertThrows(NullPointerException.class, () -> new EmpresaAnnualBilling(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-100", "-99999.99"})
    @DisplayName("Valida que el valor de la facturación anual no sea negativo")
    void shouldThrowInvalidEmpresaAnnualBillingExceptionWhenAnnualBillingIsNegative(final String input) {
        assertThrows(InvalidEmpresaAnnualBillingException.class, () -> new EmpresaAnnualBilling(new BigDecimal(input)));
    }
}