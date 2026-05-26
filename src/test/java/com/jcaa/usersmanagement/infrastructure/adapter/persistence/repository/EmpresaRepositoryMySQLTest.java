package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.*;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@DisplayName("EmpresaRepositoryMySQL")
@ExtendWith(MockitoExtension.class)
class EmpresaRepositoryMySQLTest {

    private static final String EMPRESA_ID = UUID.randomUUID().toString();
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
    private void configureStatementAndResultSet() throws SQLException {
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
    }

    // Helper: configure resultSet to return one full user row
    private void configureResultSetRow() throws SQLException {
        when(resultSet.getString("id_empresa")).thenReturn(EMPRESA_ID);
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

    // ── save() — happy path

    @Test
    @DisplayName("save() ejecuta INSERT y devuelve la empresa persistida obtenida por empresa_id")
    void shouldSaverEmpresaAndReturnById() throws SQLException {
        //Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        //Act
        final EmpresaModel result = empresaRepositoryMySQL.save(empresaModel);
        //Assert
        assertAll("save() happy path",
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

    // ── save() — INSERT fails → PersistenceException

    @Test
    @DisplayName("save() lanza una PersistenceException cuando INSERT genera una SQLException")
    void shouldThrowPersistenceExceptionWhenInsertFails() throws SQLException {
        //Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Insert failed"));
        //Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.save(empresaModel),
                "debe lanzar PersistenceException cuando INSERT genera SQLException");
    }

    // ── save() → findByIdOrFail — user not found after insert → UserNotFoundException

    @Test
    @DisplayName("save() lanza una excepción EmpresaNotFoundException cuando no se encuentra el usuario guardado")
    void shouldThrowEmpresaNotFoundExceptionWhenEmpresaNotFoundAfterSave() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);
        // Act & Assert
        assertThrows(
                EmpresaNotFoundException.class,
                () -> empresaRepositoryMySQL.save(empresaModel),
                "debe lanzar una excepción EmpresaNotFoundException cuando SELECT no devuelva filas después de INSERT");
    }

    // ── update() — happy path

    @Test
    @DisplayName("update() ejecuta UPDATE y devuelve la empresa actualizada obtenida por la id")
    void shouldUpdateEmpresaAndReturnById() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final EmpresaModel result = empresaRepositoryMySQL.update(empresaModel);
        // Assert
        assertEquals(EMPRESA_ID, result.getIdEmpresa().value(), "el id debe coincidir con la empresa actualizada");
    }

    // ── update() — UPDATE fails → PersistenceException

    @Test
    @DisplayName("update() lanza una PersistenceException cuando UPDATE lanza una SQLException")
    void shouldThrowPersistenceExceptionWhenUpdateFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.update(empresaModel),
                "debe lanzar PersistenceException cuando UPDATE genera SQLException");
    }

    // ── findById() — row found → Optional.of(empresa)

    @Test
    @DisplayName("findById() returns Optional.of(empresa) when a matching row exists")
    void shouldReturnEmpresaWhenFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findById(idEmpresa);
        // Assert
        assertAll(
                "findById() found",
                () -> assertTrue(result.isPresent(), "must be present"),
                () -> assertEquals(EMPRESA_ID, result.get().getIdEmpresa().value(), "empresa_id"));
    }

    // ── findById() — no row → Optional.empty()

    @Test
    @DisplayName("findById() devuelve Optional.empty() cuando no existe ninguna fila coincidente")
    void shouldReturnEmptyWhenNotFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findById(idEmpresa);
        // Assert
        assertTrue(result.isEmpty(), "debe devolver Optional.empty() cuando ninguna fila coincide con el id de la empresa");
    }

    // ── getById() — SQLException → PersistenceException (from prepareStatement)

    @Test
    @DisplayName("findById() lanza una PersistenceException cuando prepareStatement genera una SQLException.")
    void shouldThrowPersistenceExceptionOnFindByIdFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findById(idEmpresa),
                "must throw PersistenceException when prepareStatement raises SQLException");
    }

    // ── findById() — SQLException → PersistenceException (from executeQuery, inside try body)

    @Test
    @DisplayName("findById() throws PersistenceException when executeQuery raises SQLException")
    void shouldThrowPersistenceExceptionWhenFindByIdExecuteQueryFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findById(idEmpresa),
                "debe lanzar PersistenceException cuando executeQuery genera SQLException dentro del bloque try");
    }

    // ── findById() — SQLException → PersistenceException (from statement.close() after normal exit)

    @Test
    @DisplayName("findById() lanza PersistenceException cuando PreparedStatement.close() genera SQLException")
    void shouldThrowPersistenceExceptionWhenFindByIdStatementCloseFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(statement).close();
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findById(idEmpresa),
                "debe lanzar una PersistenceException cuando PreparedStatement.close() genere una SQLException después de una salida normal del cuerpo");
    }

    // ── findByName() — row found → Optional.of(empresa)

    @Test
    @DisplayName("findByName() devuelve Optional.of(empresa) cuando existe una fila coincidente.")
    void shouldReturnEmpresaByNameWhenFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findByName(nameEmpresa);
        // Assert
        assertAll(
                "findByName() found",
                () -> assertTrue(result.isPresent(), "must be present"),
                () -> assertEquals(EMPRESA_NAME, result.get().getNameEmpresa().value(), "name_empresa"));
    }

    // ── findByName() — no row → Optional.empty()

    @Test
    @DisplayName("findByName() devuelve Optional.empty() cuando no existe ninguna fila coincidente")
    void shouldReturnEmptyWhenNameNotFound() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(false);
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findByName(nameEmpresa);
        // Assert
        assertTrue(result.isEmpty(), "debe devolver Optional.empty() cuando ninguna fila coincida con el nombre de la empresa");
    }

    // ── findByName() — SQLException → PersistenceException (from prepareStatement)

    @Test
    @DisplayName("findByName() lanza una PersistenceException cuando prepareStatement genera una SQLException")
    void shouldThrowPersistenceExceptionOnFindByNameFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findByName(nameEmpresa),
                "debe lanzar PersistenceException cuando prepareStatement genere SQLException");
    }

    // ── findByName() — SQLException → PersistenceException (from executeQuery, inside try body)

    @Test
    @DisplayName("findByName() lanza una PersistenceException cuando executeQuery genera una SQLException")
    void shouldThrowPersistenceExceptionWhenFindMyNameExecuteQueryFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findByName(nameEmpresa),
                "debe lanzar PersistenceException cuando executeQuery genera SQLException dentro del bloque try");
    }

    // ── findByName() — SQLException → PersistenceException (from statement.close() after normal exit)

    @Test
    @DisplayName("findByName() lanza PersistenceException cuando PreparedStatement.close() genera SQLException")
    void shouldThrowPersistenceExceptionWhenFindByNameStatementCloseFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(statement).close();
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findByName(nameEmpresa),
                "debe lanzar una PersistenceException cuando PreparedStatement.close() genere una SQLException después de una salida normal del cuerpo");
    }

    // ── findAll() — happy path

    @Test
    @DisplayName("findAll() devuelve un modelo por cada fila del conjunto de resultados")
    void shouldReturnAllUsers() throws SQLException {
        // Arrange
        configureStatementAndResultSet();
        when(resultSet.next()).thenReturn(true, false);
        configureResultSetRow();
        // Act
        final List<EmpresaModel> result = empresaRepositoryMySQL.findAll();
        // Assert
        assertAll(
                "findAll() happy path",
                () -> assertEquals(1, result.size(), "list size"),
                () -> assertEquals(EMPRESA_ID, result.get(0).getIdEmpresa().value(), "first empresa id"));
    }

    // ── findAll() — SQLException → PersistenceException

    @Test
    @DisplayName("findAll() lanza una PersistenceException cuando la consulta genera una SQLException.")
    void shouldThrowPersistenceExceptionOnFindAllFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        //Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findAll(),
                "debe lanzar PersistenceException cuando SELECT genera SQLException");
    }

    // ── delete() — happy path

    @Test
    @DisplayName("delete() ejecuta DELETE sin lanzar un error")
    void shouldDeleteEmpresaWithoutThrowing() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        // Act & Assert
        assertDoesNotThrow(
                () -> empresaRepositoryMySQL.delete(idEmpresa),
                "la función delete() no debe lanzar una excepción cuando DELETE se ejecuta correctamente.");
    }

    // ── delete() — SQLException → PersistenceException

    @Test
    @DisplayName("delete() lanza una PersistenceException cuando DELETE lanza una SQLException")
    void shouldThrowPersistenceExceptionWhenDeleteFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));

        // Act + Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.delete(idEmpresa),
                "debe lanzar PersistenceException cuando DELETE genera SQLException");
    }


}