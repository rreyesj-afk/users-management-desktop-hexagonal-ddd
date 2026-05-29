package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@DisplayName("EmpresaRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class EmpresaRepositoryMySQLTest {

    private static final String EMPRESA_ID = "1";
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
    private Connection connection;
    @Mock
    private PreparedStatement statement;
    @Mock
    private ResultSet resultSet;

    private EmpresaRepositoryMySQL empresaRepositoryMySQL;
    private EmpresaModel empresaModel;
    private EmpresaId idEmpresa;
    private EmpresaName nameEmpresa;

    @BeforeEach
    void setUp(){
        empresaRepositoryMySQL = new EmpresaRepositoryMySQL(connection);
        idEmpresa = new EmpresaId(EMPRESA_ID);
        nameEmpresa = new EmpresaName(EMPRESA_NAME);
        empresaModel = new EmpresaModel(
                new EmpresaId(EMPRESA_ID),
                new EmpresaName(EMPRESA_NAME),
                new EmpresaIncorporationDate(INCORPORATION_DATE),
                new EmpresaAnnualBilling(ANNUAL_BILLING),
                new EmpresaSede(SEDE_NAME, SEDE_DESCRIPTION),
                new EmpresaSector(SECTOR_NAME, SECTOR_DESCRIPTION)
        );
    }

    // Helper: wire connection → statement → resultSet
    private void configureSelectStatement() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
    }

    private void configureInsertStatement() throws SQLException {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.getInt(1)).thenReturn(Integer.parseInt(EMPRESA_ID));
    }

    private void configureBasicStatement() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
    }

    // Helper: configure resultSet to return one full user row
    private void configureResultSetRow() throws SQLException {
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
    }

    @Test
    @DisplayName("save() ejecuta INSERT y devuelve la empresa persistida")
    void shouldSaverEmpresaAndReturnById() throws SQLException {
        // Arrange
        configureInsertStatement();
        configureSelectStatement();
        when(statement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, true);
        configureResultSetRow();
        // Act
        final EmpresaModel result = empresaRepositoryMySQL.save(empresaModel);
        // Assert
        assertAll("save() happy path",
                () -> assertEquals(EMPRESA_ID, result.getIdEmpresa().value()),
                () -> assertEquals(EMPRESA_NAME, result.getNameEmpresa().value()),
                () -> assertEquals(INCORPORATION_DATE, result.getIncorporationDate().value()),
                () -> assertEquals(ANNUAL_BILLING, result.getAnnualBilling().value())
        );
    }

    @Test
    @DisplayName("save() lanza PersistenceException cuando INSERT falla")
    void shouldThrowPersistenceExceptionWhenInsertFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Insert failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.save(empresaModel)
        );
    }

    @Test
    @DisplayName("save() lanza EmpresaNotFoundException cuando no encuentra la empresa")
    void shouldThrowEmpresaNotFoundExceptionWhenEmpresaNotFoundAfterSave() throws SQLException {
        // Arrange
        configureInsertStatement();
        configureSelectStatement();
        when(statement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(true, false);
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> empresaRepositoryMySQL.save(empresaModel)
        );
    }

    @Test
    @DisplayName("update() ejecuta UPDATE y devuelve empresa")
    void shouldUpdateEmpresaAndReturnById() throws SQLException {
        // Arrange
        configureBasicStatement();
        when(statement.executeQuery()).thenReturn(resultSet);
        when(statement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final EmpresaModel result = empresaRepositoryMySQL.update(empresaModel);
        // Assert
        assertEquals(EMPRESA_ID, result.getIdEmpresa().value());
    }

    @Test
    @DisplayName("findById() devuelve Optional.of cuando encuentra empresa")
    void shouldReturnEmpresaWhenFound() throws SQLException {
        // Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findById(idEmpresa);
        // Assert
        assertTrue(result.isPresent());
        assertEquals(EMPRESA_ID, result.get().getIdEmpresa().value());
    }

    @Test
    @DisplayName("findById() devuelve Optional.empty()")
    void shouldReturnEmptyWhenNotFound() throws SQLException {
        // Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(false);
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findById(idEmpresa);
        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("findAll() devuelve empresas")
    void shouldReturnAllUsers() throws SQLException {
        // Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(true, false);
        configureResultSetRow();
        // Act
        final List<EmpresaModel> result = empresaRepositoryMySQL.findAll();
        // Assert
        assertEquals(1, result.size());
        assertEquals(EMPRESA_ID, result.get(0).getIdEmpresa().value());
    }

    @Test
    @DisplayName("delete() ejecuta DELETE sin error")

    void shouldDeleteEmpresaWithoutThrowing() throws SQLException {
        configureBasicStatement();
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        assertDoesNotThrow(() -> empresaRepositoryMySQL.delete(idEmpresa)
        );
    }

    @Test
    @DisplayName("delete() lanza PersistenceException")
    void shouldThrowPersistenceExceptionWhenDeleteFails() throws SQLException {
        configureBasicStatement();
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));
        assertThrows(PersistenceException.class, () -> empresaRepositoryMySQL.delete(idEmpresa)
        );
    }
}