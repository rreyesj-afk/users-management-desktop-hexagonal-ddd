package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DisplayName("GetEmpresaByIdService")
@ExtendWith(MockitoExtension.class)
class GetEmpresaByIdServiceTest {

    @Mock
    private EmpresaRepositoryPort empresaRepositoryPort;

    private GetEmpresaByIdService getEmpresaByIdService;

    final String idEmpresa = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        try(final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            getEmpresaByIdService = new GetEmpresaByIdService(empresaRepositoryPort, validatorFactory.getValidator());
        }
    }

    // flujo feliz

    @Test
    @DisplayName("execute() retorna la empresa cuando el id existe")
    void shouldReturnEmpresaWhenFound() {
        //Arrange
        final GetEmpresaByIdQuery query = new GetEmpresaByIdQuery(idEmpresa);

        final EmpresaModel expected = new EmpresaModel(
                new EmpresaId(idEmpresa),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025,4,8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnología", "Desarrollo de software.")
        );

        when(empresaRepositoryPort.findById(any())).thenReturn(Optional.of(expected));
        //Act
        final EmpresaModel result = getEmpresaByIdService.execute(query);
        //Assert
        assertSame(expected, result, "La empresa no se encuentra en la BD");
    }

    @Test
    @DisplayName("execute() lanza EmpresaNotFoundException cuando el id de la empresa no existe")
    void shouldThrowWhenEmpresaNotFound() {
        // Arrange
        final GetEmpresaByIdQuery query = new GetEmpresaByIdQuery("idk, no existe");

        when(empresaRepositoryPort.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> getEmpresaByIdService.execute(query));
    }

    // ── validación del query

    @Test
    @DisplayName("execute() lanza ConstraintViolationException cuando el id de la empresa está en blanco")
    void shouldThrowWhenQueryIsInvalid() {
        // Arrange
        final GetEmpresaByIdQuery query = new GetEmpresaByIdQuery("");

        // Act & Assert
        assertThrows(ConstraintViolationException.class, () -> getEmpresaByIdService.execute(query));
        verifyNoInteractions(empresaRepositoryPort);
    }

}