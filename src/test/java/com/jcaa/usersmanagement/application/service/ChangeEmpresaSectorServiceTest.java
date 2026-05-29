package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSectorCommand;
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

@DisplayName("ChangeEmpresaSectorService")
@ExtendWith(MockitoExtension.class)
class ChangeEmpresaSectorServiceTest {

    @Mock
    private EmpresaRepositoryPort empresaRepositoryPort;
    @Mock
    private EmpresaNotificationsService empresaNotificationsService;

    private ChangeEmpresaSectorService changeEmpresaSectorService;

    @BeforeEach
    void setUp() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            changeEmpresaSectorService = new ChangeEmpresaSectorService(
                    empresaRepositoryPort,
                    empresaNotificationsService,
                    validatorFactory.getValidator()
            );
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() cambia el sector y notifica cuando empresa existe")
    void shouldChangeSectorAndNotify() {
        // Arrange
        final ChangeEmpresaSectorCommand changeEmpresaSectorCommand = new ChangeEmpresaSectorCommand("1", "Hardware", "Creación y mantenimiento de hardware.");

        final EmpresaModel currentEmpresa = EmpresaModel.create(
                new EmpresaId("1"),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnologia", "Desarrollo software")
        );

        final EmpresaModel changeSector = currentEmpresa.changeSector(
                new EmpresaSector("Hardware", "Creación y mantenimiento de hardware.")
        );

        when(empresaRepositoryPort.findEmpresaById(any())).thenReturn(Optional.of(currentEmpresa));
        when(empresaRepositoryPort.changeEmpresaSector(any())).thenReturn(changeSector);
        // Act
        final EmpresaModel result = changeEmpresaSectorService.execute(changeEmpresaSectorCommand);
        // Assert
        assertAll("flujo feliz change sector",
                () -> assertNotNull(result, "resultado no debe ser null"),
                () -> assertEquals("1", result.getIdEmpresa().value(), "id empresa"),
                () -> assertEquals("Hardware", result.getSector().sectorName(), "nombre sector actualizado"),
                () -> assertEquals("Creación y mantenimiento de hardware.", result.getSector().sectorDescription(), "descripcion sector actualizada")
        );

        verify(empresaRepositoryPort).changeEmpresaSector(any(EmpresaModel.class));
        verify(empresaNotificationsService).notifySectorChanged(changeSector);
    }

    // ── empresa no existe

    @Test
    @DisplayName("execute() lanza EmpresaNotFoundException cuando empresa no existe")
    void shouldThrowWhenEmpresaDoesNotExist() {
        // Arrange
        final ChangeEmpresaSectorCommand changeEmpresaSectorCommand = new ChangeEmpresaSectorCommand("idk, no existe", "Hardware", "Creación y mantenimiento de hardware.");

        when(empresaRepositoryPort.findEmpresaById(any())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> changeEmpresaSectorService.execute(changeEmpresaSectorCommand));
        verify(empresaRepositoryPort, never()).changeEmpresaSector(any());
        verify(empresaNotificationsService, never()).notifySedeChanged(any());
    }

    // ── command inválido

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando command es inválido")
    void shouldThrowWhenCommandIsInvalid() {
        // Arrange
        final ChangeEmpresaSectorCommand changeEmpresaSectorCommand = new ChangeEmpresaSectorCommand("", "hoal", "empty");
        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> changeEmpresaSectorService.execute(changeEmpresaSectorCommand));
        verifyNoInteractions(empresaRepositoryPort, empresaNotificationsService);
    }
}