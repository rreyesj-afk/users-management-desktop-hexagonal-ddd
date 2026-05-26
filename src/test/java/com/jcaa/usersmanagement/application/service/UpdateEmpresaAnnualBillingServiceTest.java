package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmpresaAnnualBillingCommand;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("UpdateEmpresaAnnualBillingService")
@ExtendWith(MockitoExtension.class)
class UpdateEmpresaAnnualBillingServiceTest {

    @Mock
    private EmpresaRepositoryPort empresaRepositoryPort;
    @Mock
    private EmpresaNotificationsService empresaNotificationsService;

    private UpdateEmpresaAnnualBillingService updateEmpresaAnnualBillingService;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            updateEmpresaAnnualBillingService = new UpdateEmpresaAnnualBillingService(
                    empresaRepositoryPort,
                    empresaNotificationsService,
                    validatorFactory.getValidator()
            );
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() actualiza annual billing y notifica cuando la empresa existe")
    void shouldUpdateAnnualBillingAndNotify() {
        // Arrange
        final UpdateEmpresaAnnualBillingCommand updateEmpresaAnnualBillingCommand = new UpdateEmpresaAnnualBillingCommand("1", new BigDecimal("2500000"));

        final EmpresaModel currentEmpresa = EmpresaModel.create(
                new EmpresaId("1"),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnologia", "Desarrollo software.")
        );

        final EmpresaModel updatedEmpresa = currentEmpresa.updateAnnualBilling(new EmpresaAnnualBilling(new BigDecimal("25000000")));

        when(empresaRepositoryPort.findById(any())).thenReturn(Optional.of(currentEmpresa));

        when(empresaRepositoryPort.update(any())).thenReturn(updatedEmpresa);

        // Act
        final EmpresaModel result = updateEmpresaAnnualBillingService.execute(updateEmpresaAnnualBillingCommand);

        // Assert
        assertAll("flujo feliz update annual billing",
                () -> assertNotNull(result, "resultado no debe ser null"),
                () -> assertEquals("1", result.getIdEmpresa().value(), "id empresa"),
                () -> assertEquals(new BigDecimal("25000000"), result.getAnnualBilling().value(), "facturación anual actualizada")
        );

        verify(empresaRepositoryPort).update(any(EmpresaModel.class));
        verify(empresaNotificationsService).notifyAnnualBillingUpdated(updatedEmpresa);
    }

    // ── empresa no existe

    @Test
    @DisplayName("execute() lanza EmpresaNotFoundException cuando empresa no existe")
    void shouldThrowWhenEmpresaDoesNotExist() {
        // Arrange
        final UpdateEmpresaAnnualBillingCommand updateEmpresaAnnualBillingCommand = new UpdateEmpresaAnnualBillingCommand(("idk, no existe"), new BigDecimal("2500000"));

        when(empresaRepositoryPort.findById(any())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> updateEmpresaAnnualBillingService.execute(updateEmpresaAnnualBillingCommand));
        verify(empresaRepositoryPort, never()).update(any());
        verify(empresaNotificationsService, never()).notifyAnnualBillingUpdated(any());
    }

    // ── command inválido

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando command es inválido")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange
        final UpdateEmpresaAnnualBillingCommand updateEmpresaAnnualBillingCommand = new UpdateEmpresaAnnualBillingCommand("", null);
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> updateEmpresaAnnualBillingService.execute(updateEmpresaAnnualBillingCommand));
        verifyNoInteractions(empresaRepositoryPort, empresaNotificationsService
        );
    }
}