package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidIncorporationDateException;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmpresaIncorporationDateTest {

    // --- Happy Path Tests ---

    @Test
    @DisplayName("Valida que la fecha de incorporación sea pasada")
    void shouldCreateEmpresaIncorporationDateWithValidPastDate() {
        // Arrange
        final LocalDate expectedDate = LocalDate.of(2026, 4, 8);
        // Act
        final EmpresaIncorporationDate incorporationDate = new EmpresaIncorporationDate(expectedDate);
        // Assert
        assertEquals(expectedDate, incorporationDate.value());
    }

    @Test
    @DisplayName("Valida que la fecha de incorporación de la empresa sea con fecha presente")
    void shouldCreateEmpresaIncorporationDateWithCurrentDate() {
        // Arrange
        final LocalDate today = LocalDate.now();
        // Act
        final EmpresaIncorporationDate incorporationDate = new EmpresaIncorporationDate(today);
        // Assert
        assertEquals(today, incorporationDate.value());
    }

    @Test
    @DisplayName("Retorna la fecha en cadena de texto")
    void shouldReturnStringRepresentationOfDate() {
        // Arrange
        final LocalDate date = LocalDate.of(2024, 5, 10);
        final EmpresaIncorporationDate incorporationDate = new EmpresaIncorporationDate(date);
        // Act
        final String result = incorporationDate.toString();
        // Assert
        assertEquals("2024-05-10", result);
    }

    // -- Flujo con excepciones y ramas de validación ---

    @Test
    @DisplayName("Valida que la fecha de incorporación no sea nula")
    void shouldThrowInvalidIncorporationDateExceptionWhenDateIsNull() {
        // Act & Assert
        assertThrows(InvalidIncorporationDateException.class, () -> new EmpresaIncorporationDate(null));
    }

    @Test
    @DisplayName("Valida que la fecha de incorporación no sea futura")
    void shouldThrowInvalidIncorporationDateExceptionWhenDateIsInTheFuture() {
        // Arrange
        final LocalDate futureDate = LocalDate.now().plusDays(1);
        // Act & Assert
        assertThrows(InvalidIncorporationDateException.class, () -> new EmpresaIncorporationDate(futureDate));
    }
}