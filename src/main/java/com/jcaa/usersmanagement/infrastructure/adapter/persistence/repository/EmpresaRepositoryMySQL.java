package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.EmpresaPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.EmpresaPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Log
@RequiredArgsConstructor
public final class EmpresaRepositoryMySQL implements EmpresaRepositoryPort {

    private static final String EMPRESA_INSERT_SQL =
            "INSERT INTO empresas "
            + "(empresa_name, incorporation_date, annual_billing, sede_name, sede_description, sector_name, sector_description, created_at, updated_at)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String UPDATE_EMPRESA_ANNUAL_BILLING_SQL =
            "UPDATE empresas "
            + "SET annual_billing = ?, updated_at = NOW() "
            + "WHERE id_empresa = ?";

    private static final String CHANGE_EMPRESA_SEDE_SQL =
            "UPDATE empresas "
            + "SET sede_name = ?, sede_description = ?, updated_at = NOW()"
            + "WHERE id_empresa = ?";

    private static final String CHANGE_EMPRESA_SECTOR_SQL =
            "UPDATE empresas "
            + "SET sector_name = ?, sector_description = ?, updated_at = NOW()" +
            "WHERE id_empresa = ?";

    private static final String SELECT_EMPRESA_BY_ID_SQL =
            "SELECT * "
            + "FROM empresas"
            + "WHERE id_empresa = ? LIMIT 1";

    private static final String SELECT_EMPRESA_BY_NAME_SQL =
            "SELECT * "
            + "WHERE empresa_name = ? LIMIT 1";

    private static final String SELECT_ALL_EMPRESAS_SQL =
            "SELECT * "
            + "FROM empresas "
            + "ORDER BY id_empresa ASC";

    private static final String DELETE_EMPRESA_SQL =
            "DELETE FROM empresas"
            + "WHERE id_empresa = ?";

    private final Connection connection;

    @Override
    public EmpresaModel saveEmpresa(final EmpresaModel empresa) {
        final EmpresaPersistenceDto dtoEmpresa = EmpresaPersistenceMapper.fromModelToDto(empresa);
        final String generatedId = executeSaveEmpresa(dtoEmpresa);
        return findEmpresaByIdOrFail(new EmpresaId(generatedId));
    }

    @Override
    public EmpresaModel updateEmpresaAnnualBilling(final EmpresaModel empresa) {
        final EmpresaPersistenceDto dtoEmpresa= EmpresaPersistenceMapper.fromModelToDto(empresa);
        executeUpdateEmpresaAnnualBilling(dtoEmpresa);
        return findEmpresaByIdOrFail(empresa.getIdEmpresa());
    }

    @Override
    public EmpresaModel changeEmpresaSede(final EmpresaModel empresa) {
        final EmpresaPersistenceDto dtoEmpresa= EmpresaPersistenceMapper.fromModelToDto(empresa);
        executeChangeEmpresaSede(dtoEmpresa);
        return findEmpresaByIdOrFail(empresa.getIdEmpresa());
    }

    @Override
    public EmpresaModel changeEmpresaSector(final EmpresaModel empresa) {
        final EmpresaPersistenceDto dtoEmpresa= EmpresaPersistenceMapper.fromModelToDto(empresa);
        executeChangeEmpresaSector(dtoEmpresa);
        return findEmpresaByIdOrFail(empresa.getIdEmpresa());
    }


    @Override
    public Optional<EmpresaModel> findEmpresaById(final EmpresaId idEmpresa) {
        try (final PreparedStatement statement = connection.prepareStatement(SELECT_EMPRESA_BY_ID_SQL)) {
            statement.setString(1, idEmpresa.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(EmpresaPersistenceMapper.fromResultSetToModel(resultSet));
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseFindEmpresaByIdFailed(idEmpresa.value(), exception);
        }
    }

    @Override
    public Optional<EmpresaModel> findEmpresaByName(
            final EmpresaName empresaName) {
        try (final PreparedStatement statement = connection.prepareStatement(SELECT_EMPRESA_BY_NAME_SQL)) {statement.setString(1, empresaName.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(EmpresaPersistenceMapper.fromResultSetToModel(resultSet));
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseFindEmpresaByNameFailed(empresaName.value(), exception);
        }
    }

    @Override
    public List<EmpresaModel> findAllEmpresas() {
        try (final PreparedStatement statement = connection.prepareStatement(SELECT_ALL_EMPRESAS_SQL)) {
            final ResultSet resultSet = statement.executeQuery();
            return EmpresaPersistenceMapper.fromResultSetToModelList(resultSet);
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseFindAllEmpresasFailed(exception);
        }
    }

    @Override
    public void deleteEmpresa(final EmpresaId idEmpresa) {
        try (final PreparedStatement statement = connection.prepareStatement(DELETE_EMPRESA_SQL)) {
            statement.setString(1, idEmpresa.value());
            statement.executeUpdate();
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseEmpresaDeleteFailed(idEmpresa.value(), exception);
        }
    }

    private String executeSaveEmpresa(final EmpresaPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(EMPRESA_INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dto.nameEmpresa());
            statement.setString(2, dto.incorporationDate());
            statement.setString(3, dto.annualBilling());
            statement.setString(4, dto.sedeName());
            statement.setString(5, dto.sedeDescription());
            statement.setString(6, dto.sectorName());
            statement.setString(7, dto.sectorDescription());
            statement.executeUpdate();

            try (final ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return String.valueOf(keys.getInt(1));
                }

                throw new SQLException("Could not retrieve generated ID");
            }

        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseEmpresaSaveFailed(dto.nameEmpresa(), exception);
        }
    }

    private void executeUpdateEmpresaAnnualBilling(final EmpresaPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(UPDATE_EMPRESA_ANNUAL_BILLING_SQL)) {
            statement.setString(1, dto.annualBilling());
            statement.setString(2, dto.idEmpresa());
            statement.executeUpdate();
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseEmpresaUpdateAnnualBillingFailed(dto.idEmpresa(), exception);
        }
    }

    private void executeChangeEmpresaSede(final EmpresaPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(CHANGE_EMPRESA_SEDE_SQL)) {
            statement.setString(1, dto.sedeName());
            statement.setString(2, dto.sedeDescription());
            statement.setString(3, dto.idEmpresa());
            statement.executeUpdate();
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseEmpresaChangeSedeFailed(dto.idEmpresa(), exception);
        }
    }

    private void executeChangeEmpresaSector(final EmpresaPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(CHANGE_EMPRESA_SECTOR_SQL)) {
            statement.setString(1, dto.sectorName());
            statement.setString(2, dto.sectorDescription());
            statement.setString(3, dto.idEmpresa());
            statement.executeUpdate();
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseEmpresaChangeSectorFailed(dto.idEmpresa(), exception);
        }
    }

    private EmpresaModel findEmpresaByIdOrFail(
            final EmpresaId idEmpresa) {
        return findEmpresaById(idEmpresa).orElseThrow(() -> EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.value()));
    }
}
