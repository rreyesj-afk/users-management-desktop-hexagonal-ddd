package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSedeException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EmpresaSedeTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @CsvSource({"' sede1 ', ' descripcion de la sede1 '", "'  sede1  ', '  descripcion de la sede1  '", "'sede1\t', 'descripcion de la sede1\t'"})
    @DisplayName("Valida que el nombre y descripción de la sede no tenga espacios vacíos")
    void shouldCreateEmpresaSectorMercadoWithTrimmedValues(final String sectorName, final String sectorDescription) {
        // Arrange
        final String expectedSedeName = sectorName.trim();
        final String expectedSedeDescription = sectorDescription.trim();

        // Act
        final EmpresaSector sectorMercado = new EmpresaSector(sectorName, sectorDescription);

        // Assert
        assertAll(() -> assertEquals(expectedSedeName, sectorMercado.sectorName()),
                () -> assertEquals(expectedSedeDescription, sectorMercado.sectorDescription()));
    }

    @Test
    @DisplayName("Valida que el nombre de la sede tenga al menos 3 caracteres")
    void shouldCreateEmpresaSectorMercadoWhenMinimumLengthIsValid() {
        // Arrange
        final String validSederName = "sede1"; // 3 chars
        final String validDescription = "descripcion de la sede1";
        // Act
        final EmpresaSector sectorMercado = new EmpresaSector(validSederName, validDescription);
        // Assert
        assertEquals(validSederName, sectorMercado.sectorName());
    }

    // --- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Valida que el nombre de la sede no sea nulo")
    void shouldThrowNullPointerExceptionWhenSedeNameIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new EmpresaSede(null, "descripcion de la sede1"));
    }

    @Test
    @DisplayName("Valida que la descripción de la sede no sea nulo")
    void shouldThrowNullPointerExceptionWhenSedeDescriptionIsNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> new EmpresaSede("sede1", null));
    }

    @ParameterizedTest
    @CsvSource({"'', 'descripcion de la sede1'", "'   ', 'descripcion de la sede1'", "'\t', 'descripcion de la sede1'"})
    @DisplayName("Valida que InvalidEmpresaSedeException se muestre cuando el nombre esté vacío")
    void shouldThrowInvalidEmpresaSedeExceptionWhenSedeNameIsEmpty(final String sectorName, final String sectorDescription) {
        // Act & Assert
        assertThrows(InvalidEmpresaSedeException.class, () -> new EmpresaSede(sectorName, sectorDescription));
    }

    @ParameterizedTest
    @CsvSource({"'sede1', ''", "'sede1', '   '", "'sede1', '\t'"})
    @DisplayName("Valida que InvalidEmpresaSedeException se muestre cuando la descripción esté vacía")
    void shouldThrowInvalidEmpresaSedeExceptionWhenSedeDescriptionIsEmpty(final String sectorName, final String sectorDescription) {
        // Act & Assert
        assertThrows(InvalidEmpresaSedeException.class, () -> new EmpresaSede(sectorName, sectorDescription));
    }

    @ParameterizedTest
    @CsvSource({"'1', 'sede '", "'s1', 'sede 2'"})
    @DisplayName("Valida que InvalidEmpresaSedeException se muestre cuando el nombre no tenga al menos 3 caracteres")
    void shouldThrowInvalidEmpresaSedeExceptionWhenSedeNameIsTooShort(final String sectorName, final String sectorDescription) {
        // Act & Assert
        assertThrows(InvalidEmpresaSedeException.class, () -> new EmpresaSede(sectorName, sectorDescription));
    }
}