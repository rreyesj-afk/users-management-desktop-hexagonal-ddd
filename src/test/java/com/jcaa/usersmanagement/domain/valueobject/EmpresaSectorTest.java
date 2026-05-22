package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSectorMercadoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmpresaSectorTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @CsvSource({"' sector1 ', ' descripcion del sector1 '", "'  sector1  ', '  descripcion del sector1  '", "'sector1\t', 'descripcion del sector1\t'"})
    @DisplayName("Valida que el nombre y descripción del sector no tenga espacios vacíos")
    void shouldCreateEmpresaSectorMercadoWithTrimmedValues(final String sectorName, final String sectorDescription) {
        // Arrange
        final String expectedSectorName = sectorName.trim();
        final String expectedSectorDescription = sectorDescription.trim();

        // Act
        final EmpresaSector sectorMercado = new EmpresaSector(sectorName, sectorDescription);

        // Assert
        assertAll(() -> assertEquals(expectedSectorName, sectorMercado.sectorName()),
                () -> assertEquals(expectedSectorDescription, sectorMercado.sectorDescription()));
    }

    @Test
    @DisplayName("Valida que el nombre del sector tenga al menos 3 caracteres")
    void shouldCreateEmpresaSectorMercadoWhenMinimumLengthIsValid() {
        // Arrange
        final String validSectorName = "sector1"; // 3 chars
        final String validDescription = "descripcion del sector1";
        // Act
        final EmpresaSector sectorMercado = new EmpresaSector(validSectorName, validDescription);
        // Assert
        assertEquals(validSectorName, sectorMercado.sectorName());
    }

    // --- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Valida que el nombre del sector no sea nulo")
    void shouldThrowNullPointerExceptionWhenSectorNameIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new EmpresaSector(null, "descripcion del sector1"));
    }

    @Test
    @DisplayName("Valida que la descripción del sector no sea nulo")
    void shouldThrowNullPointerExceptionWhenSectorDescriptionIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new EmpresaSector("sector1", null));
    }

    @ParameterizedTest
    @CsvSource({"'', 'descripcion del sector1'", "'   ', 'descripcion del sector1'", "'\t', 'descripcion del sector1'"})
    @DisplayName("Valida que InvalidEmpresaSectorMercadoException se muestre cuando el nombre esté vacío")
    void shouldThrowInvalidEmpresaSectorMercadoExceptionWhenSectorNameIsEmpty(final String sectorName, final String sectorDescription) {
        // Act & Assert
        assertThrows(InvalidEmpresaSectorMercadoException.class, () -> new EmpresaSector(sectorName, sectorDescription));
    }

    @ParameterizedTest
    @CsvSource({"'sector1', ''", "'sector1', '   '", "'sector1', '\t'"})
    @DisplayName("Valida que InvalidEmpresaSectorMercadoException se muestre cuando la descripción esté vacía")
    void shouldThrowInvalidEmpresaSectorMercadoExceptionWhenSectorDescriptionIsEmpty(final String sectorName, final String sectorDescription) {
        // Act & Assert
        assertThrows(InvalidEmpresaSectorMercadoException.class, () -> new EmpresaSector(sectorName, sectorDescription));
    }

    @ParameterizedTest
    @CsvSource({"'1', 'sector 1'", "'s1', 'sector 2'"})
    @DisplayName("Valida que InvalidEmpresaSectorMercadoException se muestre cuando el nombre no tenga al menos 3 caracteres")
    void shouldThrowInvalidEmpresaSectorMercadoExceptionWhenSectorNameIsTooShort(final String sectorName, final String sectorDescription) {
        // Act & Assert
        assertThrows(InvalidEmpresaSectorMercadoException.class, () -> new EmpresaSector(sectorName, sectorDescription));
    }
}