package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSedeCommand;
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

@DisplayName("ChangeEmpresaSedeService")
@ExtendWith(MockitoExtension.class)
class ChangeEmpresaSedeServiceTest {

    @Mock
    private EmpresaRepositoryPort empresaRepositoryPort;
    @Mock
    private EmpresaNotificationsService empresaNotificationsService;

    private ChangeEmpresaSedeService changeEmpresaSedeService;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            changeEmpresaSedeService = new ChangeEmpresaSedeService(
                    empresaRepositoryPort,
                    empresaNotificationsService,
                    validatorFactory.getValidator()
            );
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() cambia sede y notifica cuando empresa existe")
    void shouldChangeSedeAndNotify() {
        // Arrange
        final ChangeEmpresaSedeCommand changeEmpresaSedeCommand = new ChangeEmpresaSedeCommand("1", "Clouur", "Enfocada en hardware.");

        final EmpresaModel currentEmpresa = EmpresaModel.create(
                new EmpresaId("1"),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnologia", "Desarrollo software")
        );

        final EmpresaModel updatedEmpresa = currentEmpresa.changeSede(
                new EmpresaSede("Clouur", "Enfocada en hardware.")
        );

        when(empresaRepositoryPort.findById(any())).thenReturn(Optional.of(currentEmpresa));
        when(empresaRepositoryPort.update(any())).thenReturn(updatedEmpresa);
        // Act
        final EmpresaModel result = changeEmpresaSedeService.execute(changeEmpresaSedeCommand);
        // Assert
        assertAll("flujo feliz change sede",
                () -> assertNotNull(result, "resultado no debe ser null"),
                () -> assertEquals("1", result.getIdEmpresa().value(), "id empresa"),
                () -> assertEquals("Clouur", result.getSede().sedeName(), "nombre sede actualizado"),
                () -> assertEquals("Enfocada en hardware.", result.getSede().sedeDescription(), "descripcion sede actualizada")
        );

        verify(empresaRepositoryPort).update(any(EmpresaModel.class));
        verify(empresaNotificationsService).notifySedeChanged(updatedEmpresa);
    }

    // ── empresa no existe

    @Test
    @DisplayName("execute() lanza EmpresaNotFoundException cuando empresa no existe")
    void shouldThrowWhenEmpresaDoesNotExist() {
        // Arrange
        final ChangeEmpresaSedeCommand changeEmpresaSedeCommand = new ChangeEmpresaSedeCommand("idk, no existe", "Bogota HQ", "Nueva sede principal");

        when(empresaRepositoryPort.findById(any())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> changeEmpresaSedeService.execute(changeEmpresaSedeCommand));
        verify(empresaRepositoryPort, never()).update(any());
        verify(empresaNotificationsService, never()).notifySedeChanged(any());
    }

    // ── command inválido

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando command es inválido")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange
        final ChangeEmpresaSedeCommand changeEmpresaSedeCommand = new ChangeEmpresaSedeCommand("", "hoal", "empty");
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> changeEmpresaSedeService.execute(changeEmpresaSedeCommand));
        verifyNoInteractions(empresaRepositoryPort, empresaNotificationsService);
    }
}