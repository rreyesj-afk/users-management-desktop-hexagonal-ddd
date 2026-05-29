package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.*;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.EmpresaPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.EmpresaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("EmpresaPersistenceMapper")
@ExtendWith(MockitoExtension.class)
class EmpresaPersistenceMapperTest {

    private static final String EMPRESA_ID = "0";
    private static final String EMPRESA_NAME = "Fortuna";
    private static final LocalDate INCORPORATION_DATE = LocalDate.of(2025,4,8);
    private static final BigDecimal ANNUAL_BILLING = (new BigDecimal("1000000"));
    private static final String SEDE_NAME = "Fouver";
    private static final String SEDE_DESCRIPTION = "Enfocada en software.";
    private static final String SECTOR_NAME = "Tecnología";
    private static final String SECTOR_DESCRIPTION = "Desarrollo de software.";
    private static final String CREATED_AT = "2025-04-08 09:00:00";
    private static final String UPDATED_AT = "2025-04-09 09:00:00";

    @Mock
    private ResultSet resultSet;

    private EmpresaModel empresaModel;
    private EmpresaEntity empresaEntity;

    @BeforeEach
    void setUp(){
        empresaModel = new EmpresaModel(
                new EmpresaId(EMPRESA_ID),
                new EmpresaName(EMPRESA_NAME),
                new EmpresaIncorporationDate(INCORPORATION_DATE),
                new EmpresaAnnualBilling(ANNUAL_BILLING),
                new EmpresaSede(SEDE_NAME, SEDE_DESCRIPTION),
                new EmpresaSector(SECTOR_NAME, SECTOR_DESCRIPTION)
        );

        empresaEntity = new EmpresaEntity(
                EMPRESA_ID,
                EMPRESA_NAME,
                INCORPORATION_DATE.toString(),
                ANNUAL_BILLING.toString(),
                SEDE_NAME,
                SEDE_DESCRIPTION,
                SECTOR_NAME,
                SECTOR_DESCRIPTION,
                CREATED_AT,
                UPDATED_AT);
    }

    // ── fromModelToDto()

    @Test
    @DisplayName("fromModelToDto mapea todos los campos de EmpresaModel y asigna marcas de tiempo nulas")
    void shouldMapModelToDto(){
        //Act
        final EmpresaPersistenceDto result = EmpresaPersistenceMapper.fromModelToDto(empresaModel);
        //Assert
        assertAll("fromModelToDto",
                () -> assertEquals(EMPRESA_ID, result.idEmpresa(), "id empresa"),
                () -> assertEquals(EMPRESA_NAME, result.nameEmpresa(), "name empresa"),
                () -> assertEquals(INCORPORATION_DATE.toString(), result.incorporationDate(), "incorporation date empresa"),
                () -> assertEquals(ANNUAL_BILLING.toString(), result.annualBilling(), "annual billing"),
                () -> assertEquals(SEDE_NAME, result.sedeName(), "sede name"),
                () -> assertEquals(SEDE_DESCRIPTION, result.sedeDescription(), "sede description"),
                () -> assertEquals(SECTOR_NAME, result.sectorName(), "sector name"),
                () -> assertEquals(SECTOR_DESCRIPTION, result.sectorDescription(), "sector description"),
                () -> assertNull(result.createdAt(), "createdAt must be null"),
                () -> assertNull(result.updatedAt(), "updatedAt must be null")
        );
    }

    // ── fromEntityToModel()

    @Test
    @DisplayName("fromEntityToModel() mapea todos los campos de EmpresaEntity hacia EmpresaModel")
    void shouldMapEntityToModel(){
        //Act
        final EmpresaModel result = EmpresaPersistenceMapper.fromEntityToModel(empresaEntity);
        //Assert
        assertAll("fromEntityToModel",
                () -> assertEquals(EMPRESA_ID,          result.getIdEmpresa().value(), "id empresa"),
                () -> assertEquals(EMPRESA_NAME,        result.getNameEmpresa().value(), "name empresa"),
                () -> assertEquals(INCORPORATION_DATE,  result.getIncorporationDate().value(), "incorporation date empresa"),
                () -> assertEquals(ANNUAL_BILLING,      result.getAnnualBilling().value(), "annual billing"),
                () -> assertEquals(SEDE_NAME,           result.getSede().sedeName(), "sede name"),
                () -> assertEquals(SEDE_DESCRIPTION,    result.getSede().sedeDescription(), "sede description"),
                () -> assertEquals(SECTOR_NAME,         result.getSector().sectorName(), "sector name"),
                () -> assertEquals(SECTOR_DESCRIPTION,  result.getSector().sectorDescription(), "sector description")
                );
    }

    // ── fromResultSetToEntity() — happy path

    @Test
    @DisplayName("fromResultSetToEntity() lee todas las columnas del ResulSet")
    void shouldReadAllCollumnsFromResultSet() throws SQLException {
        //Arrange
        when(resultSet.getInt("id_empresa")).thenReturn(Integer.parseInt(EMPRESA_ID));
        when(resultSet.getString("name_empresa")).thenReturn(EMPRESA_NAME);
        when(resultSet.getString("incorporation_date")).thenReturn(INCORPORATION_DATE.toString());
        when(resultSet.getString("annual_billing")).thenReturn(ANNUAL_BILLING.toString());
        when(resultSet.getString("sede_name")).thenReturn(SEDE_NAME);
        when(resultSet.getString("sede_description")).thenReturn(SEDE_DESCRIPTION);
        when(resultSet.getString("sector_name")).thenReturn(SECTOR_NAME);
        when(resultSet.getString("sector_description")).thenReturn(SECTOR_DESCRIPTION);
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);
        //Act
        final EmpresaEntity result = EmpresaPersistenceMapper.fromResultSetToEntity(resultSet);
        //Assert
        assertAll("fromResultSetToEntity",
                () -> assertEquals(EMPRESA_ID, result.idEmpresa(), "id empresa"),
                () -> assertEquals(EMPRESA_NAME, result.nameEmpresa(), "name empresa"),
                () -> assertEquals(INCORPORATION_DATE.toString(), result.incorporationDate(), "incorporation date empresa"),
                () -> assertEquals(ANNUAL_BILLING.toString(), result.annualBilling(), "annual billing"),
                () -> assertEquals(SEDE_NAME, result.sedeName(), "sede name"),
                () -> assertEquals(SEDE_DESCRIPTION, result.sedeDescription(), "sede description"),
                () -> assertEquals(SECTOR_NAME, result.sectorName(), "sector name"),
                () -> assertEquals(SECTOR_DESCRIPTION, result.sectorDescription(), "sector description"),
                () -> assertEquals(CREATED_AT, result.createdAt(), "createdAt must be null"),
                () -> assertEquals(UPDATED_AT, result.updatedAt(), "updatedAt must be null")
        );
    }

    // ── fromResultSetToEntity() — SQLException propagation

    @Test
    @DisplayName("fromResultSetToEntity() propaga SQLException cuando falla la lectura de ResultSet")
    void shouldPropagateExceptionFromResultSet() throws SQLException {
        //Arrange
        when(resultSet.getString(anyString())).thenThrow(new SQLException("Column read failed"));
        //Act & Assert
        assertThrows(SQLException.class,
                () -> EmpresaPersistenceMapper.fromResultSetToEntity(resultSet),
                "debe propagarse SQLException cuando ResultSet lanza en getString");
    }

    // ── fromResultSetToModelList() — empty

    @Test
    @DisplayName("fromResultSetToModelList() revuelve una lista vacía cuando ResultSet no tiene filas")
    void shouldReturnEmptyListWhenResultSetIsEmpty() throws SQLException {
        //Arrange
        when(resultSet.next()).thenReturn(false);
        //Act
        final List<EmpresaModel> result = EmpresaPersistenceMapper.fromResultSetToModelList(resultSet);
        //Assert
        assertTrue(result.isEmpty(), "debe devolver una lista vacía cuando ResultSet no tenga filas");
    }

    // ── fromResultSetToModelList() — multiple rows

    @Test
    @DisplayName("fromResultSetToModelList() devuelve un modelo por fila en el ResultSet")
    void shouldReturnOneModelPerRow() throws SQLException {
        //Arrange
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("id_empresa")).thenReturn(Integer.parseInt(EMPRESA_ID));
        when(resultSet.getString("name_empresa")).thenReturn(EMPRESA_NAME);
        when(resultSet.getString("incorporation_date")).thenReturn(INCORPORATION_DATE.toString());
        when(resultSet.getString("annual_billing")).thenReturn(ANNUAL_BILLING.toString());
        when(resultSet.getString("sede_name")).thenReturn(SEDE_NAME);
        when(resultSet.getString("sede_description")).thenReturn(SEDE_DESCRIPTION);
        when(resultSet.getString("sector_name")).thenReturn(SECTOR_NAME);
        when(resultSet.getString("sector_description")).thenReturn(SECTOR_DESCRIPTION);
        when(resultSet.getString("created_at")).thenReturn(CREATED_AT);
        when(resultSet.getString("updated_at")).thenReturn(UPDATED_AT);
        //Act
        final List<EmpresaModel> result = EmpresaPersistenceMapper.fromResultSetToModelList(resultSet);
        //Assert
        assertEquals(2,result.size(), "debe devolver un modelo por fila en el ResultSet");
    }

    // ── fromResultSetToModelList() — SQLException propagation during iteration

    @Test
    @DisplayName("fromResultSetToModelList() propaga una SQLException cuando falla la lectura de una fila")
    void shouldPropagateExceptionDuringIteration() throws SQLException {
        //Arrange
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString(anyString())).thenThrow(new SQLException("falló la lectura de la fila"));
        //Act & Assert
        assertThrows(SQLException.class,
                () -> EmpresaPersistenceMapper.fromResultSetToModelList(resultSet),
                "se debe propagar una SQLException cuando no se pueda leer una fila"
        );
    }
}