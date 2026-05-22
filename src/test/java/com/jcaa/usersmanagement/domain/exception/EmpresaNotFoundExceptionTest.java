package com.jcaa.usersmanagement.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EmpresaNotFoundExceptionTest {

    @Test
    @DisplayName("Validar que se muestre el ID de la empresa al mostrar el error: becauseIdWasNotFound()")
    void shouldCreateExceptionWithCorrectMessage() {
        // Arrange
        final String empresaId = "NoFortuna";
        // Act
        final String message = EmpresaNotFoundException.becauseIdWasNotFound(empresaId).getMessage();
        // Assert
        assertTrue(message.contains(empresaId), "El mensaje debe identificar la empresa no encontrada");
    }
}