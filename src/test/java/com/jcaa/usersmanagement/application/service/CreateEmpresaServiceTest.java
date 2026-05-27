package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmpresaCommand;
import com.jcaa.usersmanagement.domain.exception.EmpresaAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.*;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateEmpresaService")
@ExtendWith(MockitoExtension.class)
class CreateEmpresaServiceTest {

    @Mock
    private EmpresaRepositoryPort empresaRepositoryPort;
    @Mock
    private EmpresaNotificationsService empresaNotificationService;
    private CreateEmpresaService createEmpresaService;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            createEmpresaService = new CreateEmpresaService(
                    empresaRepositoryPort,
                    empresaNotificationService,
                    validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() guarda empresa y envía notificación cuando el nombre no existe")
    void shouldSaveEmpresaAndNotifyWhenNameIsNew() {
        // Arrange
        final CreateEmpresaCommand command = new CreateEmpresaCommand(
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnologia",
                "Desarrollo de software");

        final EmpresaModel savedEmpresa = EmpresaModel.create(
                new EmpresaId("1"),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("1000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnologia", "Desarrollo de software"));

        when(empresaRepositoryPort.findByName(any())).thenReturn(Optional.empty());
        when(empresaRepositoryPort.save(any())).thenReturn(savedEmpresa);
        // Act
        final EmpresaModel result = createEmpresaService.execute(command);
        // Assert
        assertAll("flujo feliz CreateEmpresaService",
                () -> assertNotNull(result, "resultado no debe ser null"),
                () -> assertEquals("Fortuna", result.getNameEmpresa().value(), "nombre empresa"),
                () -> assertEquals("1", result.getIdEmpresa().value(), "id empresa"));
        verify(empresaRepositoryPort).save(any(EmpresaModel.class));
        verify(empresaNotificationService).notifyEmpresaCreated(savedEmpresa);
    }

    // ── empresa duplicada

    @Test
    @DisplayName("execute() lanza EmpresaAlreadyExistsException cuando el nombre ya existe")
    void shouldThrowWhenEmpresaAlreadyExists() {
        // Arrange
        final CreateEmpresaCommand command = new CreateEmpresaCommand(
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnologia",
                "Desarrollo de software.");

        final EmpresaModel existingEmpresa = EmpresaModel.create(
                new EmpresaId("1"),
                new EmpresaName("Fouver"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("1000000")),
                new EmpresaSede("HQE", "principal"),
                new EmpresaSector("Tecnologia", "software"));

        when(empresaRepositoryPort.findByName(any())).thenReturn(Optional.of(existingEmpresa));
        // Act & Assert
        assertThrows(EmpresaAlreadyExistsException.class, () -> createEmpresaService.execute(command));
        verify(empresaRepositoryPort, never()).save(any());
        verify(empresaNotificationService, never()).notifyEmpresaCreated(any());
    }

    // ── validación del command

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el command es inválido")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange
        final CreateEmpresaCommand command = new CreateEmpresaCommand(
                "",
                LocalDate.now().plusDays(1),
                new BigDecimal("-1"),
                "",
                "",
                "",
                "");
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> createEmpresaService.execute(command));
        verifyNoInteractions(empresaRepositoryPort, empresaNotificationService);
    }
}