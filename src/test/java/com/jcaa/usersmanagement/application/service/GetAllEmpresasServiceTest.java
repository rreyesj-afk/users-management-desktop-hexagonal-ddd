package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@DisplayName("GetAllEmpresasService")
class GetAllEmpresasServiceTest {

    @Mock
    private EmpresaRepositoryPort  empresaRepositoryPort;

    private GetAllEmpresasService getAllEmpresasService;

    final String idEmpresa = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        getAllEmpresasService = new GetAllEmpresasService(empresaRepositoryPort);
    }


    @Test
    @DisplayName("execute() retorna la lista de usuarios del puerto")
    void shouldReturnEmpresasFromPort() {
        //Arrange
        final EmpresaModel empresas = new EmpresaModel(
                new EmpresaId(idEmpresa),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnología", "Desarrollo de software.")
        );

        when(empresaRepositoryPort.findAll()).thenReturn(List.of(empresas));
        //Act
        final List<EmpresaModel> result = getAllEmpresasService.execute();
        //Assert
        assertAll("findAll con una empresa",
                () -> assertEquals(1, result.size(), "debe retornar una empresa"),
                () -> assertSame(empresas, result.get(0), "debe ser el mismo objeto del puerto"));
    }

    @Test
    @DisplayName("execute() retorna lista vacía cuando no hay empresas")
    void shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(empresaRepositoryPort.findAll()).thenReturn(List.of());
        // Act
        final List<EmpresaModel> result = getAllEmpresasService.execute();
        // Assert
        assertTrue(result.isEmpty(), "debe retornar lista vacía");
    }
}