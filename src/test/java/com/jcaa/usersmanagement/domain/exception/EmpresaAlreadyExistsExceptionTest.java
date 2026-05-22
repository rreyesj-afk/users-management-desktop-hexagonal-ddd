package com.jcaa.usersmanagement.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EmpresaAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Validar que se muestre el nombre de la empresa existente")
    void shouldCreateExceptionWithCorrectMessage() {
        // Arrange
        final String empresaName = "Fortuna";
        // Act
        final String message = EmpresaAlreadyExistsException.becauseNameAlreadyExists(empresaName).getMessage();
        // Assert
        assertTrue(message.contains(empresaName),"Ya existe una empresa con ese nombre.");
    }
}
