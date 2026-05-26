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

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Log
@RequiredArgsConstructor
public final class EmpresaRepositoryMySQL implements EmpresaRepositoryPort {

    private static final String INSERT_SQL =
            "INSERT INTO empresas "
            + "(empresa_name, incorporation_date, annual_billing, sede_name, sede_description, sector_name, sector_description, created_at, updated_at)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String UPDATE_SQL =
            "UPDATE empresas SET empresa_name = ?, incorporation_date = ?, annual_billing = ?, sede_name = ?, sede_description = ?, sector_name = ?, sector_description = ?, updated_at = NOW()"
            + "WHERE id_empresa = ?";

    public static final String SELECT_BY_ID_SQL =
            "SELECT * "
            + "FROM empresas"
            + "WHERE id_empresa = ? LIMIT 1";

    public static final String SELECT_BY_NAME_SQL =
            "SELECT * "
            + "WHERE empresa_name = ? LIMIT 1";

    public static final String SELECT_ALL_SQL =
            "SELECT * "
            + "FROM empresas "
            + "ORDER BY id_empresa ASC";

    private static final String DELETE_SQL =
            "DELETE FROM empresas"
            + "WHERE id_empresa = ?";

    private final Connection connection;

    @Override
    public EmpresaModel save(final EmpresaModel empresa) {
        final EmpresaPersistenceDto dto = EmpresaPersistenceMapper.fromModelToDto(empresa);
        final String generatedId = executeSave(dto);
        return findByIdOrFail(new EmpresaId(generatedId));
    }

    @Override
    public EmpresaModel update(final EmpresaModel empresa) {
        final EmpresaPersistenceDto dto = EmpresaPersistenceMapper.fromModelToDto(empresa);
        executeUpdate(dto);
        return findByIdOrFail(empresa.getIdEmpresa());
    }

    @Override
    public Optional<EmpresaModel> findById(final EmpresaId idEmpresa) {
        try (final PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setString(1, idEmpresa.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(EmpresaPersistenceMapper.fromResultSetToModel(resultSet));
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseFindByIdFailed(idEmpresa.value(), exception);
        }
    }

    @Override
    public Optional<EmpresaModel> findByName(
            final EmpresaName empresaName) {
        try (final PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_SQL)) {statement.setString(1, empresaName.value());
            final ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }

            return Optional.of(EmpresaPersistenceMapper.fromResultSetToModel(resultSet));
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseFindByNameFailed(empresaName.value(), exception);
        }
    }

    @Override
    public List<EmpresaModel> findAll() {
        try (final PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL)) {
            final ResultSet resultSet = statement.executeQuery();
            return EmpresaPersistenceMapper.fromResultSetToModelList(resultSet);
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseFindAllFailed(exception);
        }
    }

    @Override
    public void delete(final EmpresaId idEmpresa) {
        try (final PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setString(1, idEmpresa.value());
            statement.executeUpdate();
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseDeleteFailed(idEmpresa.value(), exception);
        }
    }

    private String executeSave(final EmpresaPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
            throw PersistenceException.becauseSaveFailed(dto.idEmpresa(), exception);
        }
    }

    private void executeUpdate(final EmpresaPersistenceDto dto) {
        try (final PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, dto.nameEmpresa());
            statement.setString(2, dto.incorporationDate());
            statement.setBigDecimal(3, new BigDecimal(dto.annualBilling()));
            statement.setString(4, dto.sedeName());
            statement.setString(5, dto.sedeDescription());
            statement.setString(6, dto.sectorName());
            statement.setString(7, dto.sectorDescription());
            statement.setString(8, dto.idEmpresa());
            statement.executeUpdate();
        }

        catch (final SQLException exception) {
            throw PersistenceException.becauseUpdateFailed(dto.idEmpresa(), exception);
        }
    }


    private EmpresaModel findByIdOrFail(
            final EmpresaId idEmpresa) {
        return findById(idEmpresa).orElseThrow(() -> EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.value()));
    }
}