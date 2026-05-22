package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaIdException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EmpresaIdTest {

    // --- Happy Path Tests ---

    @ParameterizedTest
    @ValueSource(strings = {" empresa1 ", "  empresa1  ", "empresa1\t"})
    @DisplayName("Valida que no haya espacios vacíos.")
    void shouldCreateEmpresaIdWithTrimmedValue(String input) {
        // Arrange
        final String correctEmpresaId = "empresa1";
        // Act
        final EmpresaId empresaId = new EmpresaId(input);
        // Assert
        assertEquals(correctEmpresaId, empresaId.toString());
    }

    @Test
    @DisplayName("Valida que se genere una ID válida")
    void shouldGenerateValidUuid() {
        final EmpresaId empresaId = EmpresaId.newId();
        assertDoesNotThrow(() -> UUID.fromString(empresaId.value()));
    }

    @Test
    @DisplayName("Valida que el campo EmpresaId no sea nulo")
    void shouldThrowInvalidEmpresaIdExceptionWhenEmpresaIdIsEmpty() {
        assertThrows(NullPointerException.class, () -> new EmpresaId(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f"})
    @DisplayName("Muestra InvalidEmpresaIdException cuando EmpresaId está vacío")
    void shouldThrowInvalidEmpresaIdExceptionWhenEmpresaIdIsEmpty(final String input) {
        // Act & Assert
        assertThrows(InvalidEmpresaIdException.class, () -> new EmpresaId(input));
    }

    @Test
    @DisplayName("Valida que se generen IDs diferentes") void shouldGenerateDifferentIds() {
        // Arrange
        final EmpresaId firstId = EmpresaId.newId();
        // Act
        final EmpresaId secondId = EmpresaId.newId();
        // Assert
        assertNotEquals(firstId, secondId);
    }
}
