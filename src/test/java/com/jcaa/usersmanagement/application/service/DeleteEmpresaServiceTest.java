package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmpresaCommand;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("DeleteEmpresaService")
@ExtendWith(MockitoExtension.class)
class DeleteEmpresaServiceTest {

    @Mock
    private EmpresaRepositoryPort empresaRepositoryPort;

    private DeleteEmpresaService deleteEmpresaService;

    @BeforeEach
    void setUp() {
        try(final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            deleteEmpresaService = new DeleteEmpresaService(empresaRepositoryPort, validatorFactory.getValidator());
        }
    }

    // ── flujo feliz

    @Test
    @DisplayName("execute() invoca empresaRepositoryPort cuando la empresa existe")
    void shouldDeleteWhenEmpresaExists() {
        //Arrange
        final DeleteEmpresaCommand deleteEmpresaCommand = new DeleteEmpresaCommand("1");

         final EmpresaModel existingEmpresa = new EmpresaModel(
                 new EmpresaId("1"),
                 new EmpresaName("Fortuna"),
                 new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                 new EmpresaAnnualBilling(new BigDecimal("10000000")),
                 new EmpresaSede("Fouver", "Enfocada en software."),
                 new EmpresaSector("Tecnología.", "Desarrollo de software.")
         );

         when(empresaRepositoryPort.findEmpresaById(any())).thenReturn(Optional.of(existingEmpresa));
         //Act
        deleteEmpresaService.execute(deleteEmpresaCommand);
        //Assert
        verify(empresaRepositoryPort).deleteEmpresa(new EmpresaId("1"));
   }

   // empresa no encontrada

    @Test
    @DisplayName("execute() lanza EmpresaNotFoundException cuando el id de la empresa no existe")
    void shouldThrowWhenEmpresaNotFound(){
        // Arrange
        final DeleteEmpresaCommand deleteEmpresaCommand = new DeleteEmpresaCommand("idk, no existe");

        when(empresaRepositoryPort.findEmpresaById(any())).thenReturn(Optional.empty());
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> deleteEmpresaService.execute(deleteEmpresaCommand));
        verify(empresaRepositoryPort, never()).deleteEmpresa(any());
    }

    // validación del command

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el id de la empresa está vacío")
    void shouldThrowWhenCommandIsInvalid(){
        //Arrange
        final DeleteEmpresaCommand deleteEmpresaCommand = new DeleteEmpresaCommand("   ");
        //Act & Assert
        assertThrows(ConstraintViolationException.class, () ->  deleteEmpresaService.execute(deleteEmpresaCommand));
        verifyNoInteractions(empresaRepositoryPort);
    }



}