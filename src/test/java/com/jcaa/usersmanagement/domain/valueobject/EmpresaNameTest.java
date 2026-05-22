package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmpresaNameTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(strings = {"Fortuna", "  Fortuna  ", "  Fortuna\t" })
    @DisplayName("Valida que el nombre de la empresa no tenga espacios vacíos")
    void shouldCreateEmpresaNameWithTrimmedValue(final String input) {
        // Arrange
        final String correctEmpresaName = "Fortuna";
        // Act
        final EmpresaName empresaNameVo = new EmpresaName(input);
        // Assert
        assertEquals(correctEmpresaName, empresaNameVo.toString());

    }

    @Test
    @DisplayName("Valida que el nombre de la empresa tenga al menos 5 caracteres")
    void shouldCreateEmpresaNameWhenMinimumLengthIsValid() {
        // Arrange
        final String validEmpresaName = "Fortuna";
        // Act
        final EmpresaName empresaName = new EmpresaName(validEmpresaName);
        // Assert
        assertEquals(validEmpresaName, empresaName.value());
    }

    @Test
    @DisplayName("Valida que el nombre de la empresa no sea nulo")
    void shouldValidateEmpresaNameIsNotNull(){assertThrows(NullPointerException.class, () -> new EmpresaName(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f"})
    @DisplayName("Valida que InvalidEmpresaNameException se muestre cuando el nombre esté vacío")
    void shouldThrowInvalidEmpresaNameExceptionWhenEmpresaNameIsEmpty(final String input) {
        assertThrows(InvalidEmpresaNameException.class, () -> new EmpresaName(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"A", "AB", "ABC", "ABCD"})
    @DisplayName("Valida que InvalidEmpresaNameException se muestre cuando en nombre sea muy corto")
     void shouldThrowInvalidEmpresaNameExceptionWhenEmpresaNameIsTooShort(final String input) {
        assertThrows(InvalidEmpresaNameException.class, () -> new EmpresaName(input));
    }
}