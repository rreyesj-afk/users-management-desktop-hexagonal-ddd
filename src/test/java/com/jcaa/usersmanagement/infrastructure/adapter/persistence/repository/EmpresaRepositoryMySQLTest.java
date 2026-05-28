package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
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
import static org.mockito.Mockito.*;

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
    private PreparedStatement selectStatement;
    @Mock
    private ResultSet resultSet;

    @Mock
    private ResultSet generatedKeysResultSet;
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
        when(connection.prepareStatement(anyString())).thenReturn(selectStatement);
        when(selectStatement.executeQuery()).thenReturn(resultSet);
    }

    private void configureInsertStatement() throws SQLException {
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(generatedKeysResultSet);
        when(generatedKeysResultSet.next()).thenReturn(true);

        when(generatedKeysResultSet.getInt(1)).thenReturn(Integer.parseInt(EMPRESA_ID));
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

    // ==================================================
    // saveEmpresa()
    // ==================================================

    @Test
    @DisplayName("saveEmpresa() ejecuta INSERT y devuelve empresa persistida")
    void shouldSaveEmpresaAndReturnById() throws SQLException {
        // Arrange
        configureInsertStatement();
        configureSelectStatement();
        when(statement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final EmpresaModel result = empresaRepositoryMySQL.saveEmpresa(empresaModel);
        // Assert
        assertAll("saveEmpresa() happy path",
                () -> assertEquals(EMPRESA_ID, result.getIdEmpresa().value()),
                () -> assertEquals(EMPRESA_NAME, result.getNameEmpresa().value()),
                () -> assertEquals(INCORPORATION_DATE, result.getIncorporationDate().value()),
                () -> assertEquals(ANNUAL_BILLING, result.getAnnualBilling().value()),
                () -> assertEquals(SEDE_NAME, result.getSede().sedeName()),
                () -> assertEquals(SEDE_DESCRIPTION, result.getSede().sedeDescription()),
                () -> assertEquals(SECTOR_NAME, result.getSector().sectorName()),
                () -> assertEquals(SECTOR_DESCRIPTION, result.getSector().sectorDescription())
        );
    }

    // saveEmpresa() - INSERT fails → PersistenceException

    @Test
    @DisplayName("saveEmpresa() lanza PersistenceException cuando INSERT falla")
    void shouldThrowPersistenceExceptionWhenInsertFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)
        )).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Insert failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.saveEmpresa(empresaModel)
        );
    }

    // saveEmpresa() → findByIdOrFail — empresa not found after insert → EmpresaNotFoundException

    @Test
    @DisplayName("saveEmpresa() throws EmpresaNotFoundException when the saved empresa cannot be found")
    void shouldThrowEmpresaNotFoundExceptionWhenEmpresaNotFoundAfterSave() throws SQLException {
        // Arrange
        configureInsertStatement();
        configureSelectStatement();
        when(statement.executeUpdate()).thenReturn(1);
        when(resultSet.next()).thenReturn(false);
        // Act + Assert
        assertThrows(
                EmpresaNotFoundException.class,
                () -> empresaRepositoryMySQL.saveEmpresa(empresaModel)
        );
    }

    // ==================================================
    // updateAnnualBilling()
    // ==================================================

    @Test
    @DisplayName("updateAnnualBilling() ejecuta UPDATE_EMPRESA_ANNUAL_BILLING y devuelve empresa actualizada")
    void shouldUpdateAnnualBillingAndReturnEmpresa() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement, selectStatement);
        when(statement.executeUpdate()).thenReturn(1);
        when(selectStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final EmpresaModel result = empresaRepositoryMySQL.updateEmpresaAnnualBilling(empresaModel);
        // Assert
        assertAll("updated annual billing empresa",
                () -> assertEquals(EMPRESA_ID, result.getIdEmpresa().value()),
                () -> verify(statement).setString(1, ANNUAL_BILLING.toString()),
                () -> verify(statement).setString(2, EMPRESA_ID),
                () -> verify(statement).executeUpdate()
        );
    }

    // updateEmpresaAnnualBilling() - UPDATE fails → PersistenceException

    @Test
    @DisplayName("updateEmpresaAnnualBilling() throws PersistenceException when UPDATE raises SQLException")
    void shouldThrowPersistenceExceptionWhenUpdateAnnualBillingFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.updateEmpresaAnnualBilling(empresaModel));
    }

    // ==================================================
    // changeSede()
    // ==================================================

    @Test
    @DisplayName("changeEmpresaSede() ejecuta UPDATE y devuelve empresa actualizada")
    void shouldChangeSedeAndReturnEmpresa() throws SQLException {
        //Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement, selectStatement);
        when(statement.executeUpdate()).thenReturn(1);
        when(selectStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        //Act
        final EmpresaModel result = empresaRepositoryMySQL.changeEmpresaSede(empresaModel);
        //Assert
        assertAll("changed sede empresa",
                () -> assertEquals(EMPRESA_ID, result.getIdEmpresa().value()),
                () -> verify(statement).setString(1, SEDE_NAME),
                () -> verify(statement).setString(2, SEDE_DESCRIPTION),
                () -> verify(statement).setString(3, EMPRESA_ID)
        );
    }

    // changeEmpresaSede() - UPDATE fails → PersistenceException

    @Test
    @DisplayName("changeEmpresaSede() throws PersistenceException when UPDATE raises SQLException")
    void shouldThrowPersistenceExceptionWhenUpdateSedeFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.changeEmpresaSede(empresaModel)
        );
    }

    // ==================================================
    // changeSector()
    // ==================================================

    @Test
    @DisplayName("changeEmpresaSector() ejecuta UPDATE y devuelve empresa actualizada")
    void shouldChangeSectorAndReturnEmpresa() throws SQLException {
        //Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement, selectStatement);
        when(statement.executeUpdate()).thenReturn(1);
        when(selectStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        //Act
        final EmpresaModel result = empresaRepositoryMySQL.changeEmpresaSector(empresaModel);
        //Asser
        assertAll("changed sector empresa",
                () -> assertEquals(EMPRESA_ID, result.getIdEmpresa().value()),
                () -> verify(statement).setString(1, SECTOR_NAME),
                () -> verify(statement).setString(2, SECTOR_DESCRIPTION),
                () -> verify(statement).setString(3, EMPRESA_ID)
        );
    }

    // changeEmpresaSector() - UPDATE fails → PersistenceException

    @Test
    @DisplayName("changeEmpresaSector() throws PersistenceException when UPDATE raises SQLException")
    void shouldThrowPersistenceExceptionWhenUpdateSectorFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeUpdate()).thenThrow(new SQLException("Update failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.changeEmpresaSector(empresaModel)
        );
    }

    // ==================================================
    // findById()
    // ==================================================

    @Test
    @DisplayName("findEmpresaById() devuelve Optional.of(empresa) cuando encuentra empresa")
    void shouldReturnEmpresaWhenFound() throws SQLException {
        // Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findEmpresaById(idEmpresa);
        // Assert
        assertAll("findEmpresaById() found",
                () -> assertTrue(result.isPresent(), "must be present"),
                () -> assertEquals(EMPRESA_ID, result.get().getIdEmpresa().value())
        );
    }

    // findEmpresaById() — no row → Optional.empty()

    @Test
    @DisplayName("findEmpresaById() devuelve Optional.empty()")
    void shouldReturnEmptyWhenNotFound() throws SQLException {
        // Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(false);
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findEmpresaById(idEmpresa);
        // Assert
        assertTrue(result.isEmpty());
    }

    // ── findEmpresaById() — SQLException → PersistenceException (from prepareStatement)

    @Test
    @DisplayName("findEmpresaById() throws PersistenceException when prepareStatement raises SQLException")
    void shouldThrowPersistenceExceptionOnFindEmpresaByIdFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findEmpresaById(idEmpresa));
    }

    // ── findEmpresaById() — SQLException → PersistenceException (from executeQuery, inside try body)

    @Test
    @DisplayName("findEmpresaById() throws PersistenceException when executeQuery raises SQLException")
    void shouldThrowPersistenceExceptionWhenFindEmpresaByIdExecuteQueryFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findEmpresaById(idEmpresa));
    }

    // ── findEmpresaById() — SQLException → PersistenceException (from statement.close() after normal exit)

    @Test
    @DisplayName("findEmpresaById() throws PersistenceException when PreparedStatement.close() raises SQLException")
    void shouldThrowPersistenceExceptionWhenFindEmpresaByIdStatementCloseFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(statement).close();
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findEmpresaById(idEmpresa));
    }

    // ==================================================
    // findEmpresaByName()
    // ==================================================

    @Test
    @DisplayName("findEmpresaByName() devuelve Optional.of")
    void shouldReturnEmpresaWhenFoundByName() throws SQLException {
        //Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(true);
        configureResultSetRow();
        //Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findEmpresaByName(nameEmpresa);
        assertAll("findEmpresaByName() found",
                () -> assertTrue(result.isPresent()),
                () -> assertEquals(EMPRESA_NAME, result.get().getNameEmpresa().value())
        );
    }

    // ── findEmpresaByName() — no row → Optional.empty()

    @Test
    @DisplayName("findEmpresaByName() returns Optional.empty() when no matching row exists")
    void shouldReturnEmptyWhenEmpresaNotFoundByName() throws SQLException {
        // Arrange
        configureSelectStatement();
        when(resultSet.next()).thenReturn(false);
        // Act
        final Optional<EmpresaModel> result = empresaRepositoryMySQL.findEmpresaByName(nameEmpresa);
        // Assert
        assertTrue(result.isEmpty());
    }

    // ── findEmpresaByName() — SQLException → PersistenceException (from prepareStatement)

    @Test
    @DisplayName("findEmpresaByName() throws PersistenceException when prepareStatement raises SQLException")
    void shouldThrowPersistenceExceptionFindEmpresaByNameFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findEmpresaByName(nameEmpresa));
    }

    // ── findEmpresaByName() — SQLException → PersistenceException (from executeQuery, inside try body)

    @Test
    @DisplayName("findEmpresaByName() throws PersistenceException when executeQuery raises SQLException")
    void shouldThrowPersistenceExceptionWhenFindEmpresaByNameExecuteQueryFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenThrow(new SQLException("Execute query failed"));
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findEmpresaByName(nameEmpresa));
    }

    // ── findEmpresaByName() — SQLException → PersistenceException (from statement.close() after normal
    // exit)

    @Test
    @DisplayName("findEmpresaByName() throws PersistenceException when PreparedStatement.close() raises SQLException")
    void shouldThrowPersistenceExceptionWhenFindEmpresaByNameStatementCloseFails() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        doThrow(new SQLException("Close failed")).when(statement).close();
        // Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.findEmpresaByName(nameEmpresa));
    }

    // ==================================================
    // findAllEmpresas()
    // ==================================================

    @Test
    @DisplayName("findAllEmpresas() devuelve empresas")
    void shouldReturnAllEmpresas() throws SQLException {
        configureSelectStatement();
        when(resultSet.next()).thenReturn(true, false);
        configureResultSetRow();
        final List<EmpresaModel> result = empresaRepositoryMySQL.findAllEmpresas();
        assertAll("findAllEmpresas() happy path",
                () -> assertEquals(1, result.size()),
                () -> assertEquals(EMPRESA_ID, result.get(0).getIdEmpresa().value())
        );
    }

    // ── findAllEmpresas() — SQLException → PersistenceException

    @Test
    @DisplayName("findAllEmpresas() throws PersistenceException when the query raises SQLException")
    void shouldThrowPersistenceExceptionOnFindAllEmpresasFailure() throws SQLException {
        // Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        // Act & Assert
        assertThrows(PersistenceException.class,
                () -> empresaRepositoryMySQL.findAllEmpresas());
    }

    // ==================================================
    // deleteEmpresa()
    // ==================================================

    @Test
    @DisplayName("deleteEmpresa() ejecuta DELETE sin error")
    void shouldDeleteEmpresaWithoutThrowing() throws SQLException {
        //Arrange
        when(connection.prepareStatement(anyString())).thenReturn(statement);
        //Act & Assert
        assertDoesNotThrow(
                () -> empresaRepositoryMySQL.deleteEmpresa(idEmpresa)
        );
    }

    // deleteEmpresa() — SQLException → PersistenceException

    @Test
    @DisplayName("deleteEmpresa() lanza PersistenceException")
    void shouldThrowPersistenceExceptionWhenDeleteFails() throws SQLException {
        //Arrange
        when(connection.prepareStatement(anyString())).thenThrow(new SQLException("Delete failed"));
        //Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> empresaRepositoryMySQL.deleteEmpresa(idEmpresa)
        );
    }
}