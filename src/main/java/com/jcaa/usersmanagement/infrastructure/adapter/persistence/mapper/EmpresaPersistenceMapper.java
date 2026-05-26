package com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.*;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.EmpresaPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity.EmpresaEntity;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EmpresaPersistenceMapper {

    public EmpresaPersistenceDto fromModelToDto(final EmpresaModel empresa) {
        return new EmpresaPersistenceDto(
                empresa.getIdEmpresa().value(),
                empresa.getNameEmpresa().value(),
                empresa.getIncorporationDate().value().toString(),
                empresa.getAnnualBilling().value().toString(),
                empresa.getSede().sedeName(),
                empresa.getSede().sedeDescription(),
                empresa.getSector().sectorName(),
                empresa.getSector().sectorDescription(),
                null,
                null
        );
    }

    public EmpresaEntity fromResultSetToEntity(final ResultSet resultSet) throws SQLException {
        return new EmpresaEntity(
                String.valueOf(resultSet.getInt("id_empresa")),
                resultSet.getString("name_empresa"),
                resultSet.getString("incorporation_date"),
                resultSet.getString("annual_billing"),
                resultSet.getString("sede_name"),
                resultSet.getString("sede_description"),
                resultSet.getString("sector_name"),
                resultSet.getString("sector_description"),
                resultSet.getString("created_at"),
                resultSet.getString("updated_at")
        );
    }

    public EmpresaModel fromEntityToModel(final EmpresaEntity empresaEntity) {
        return new EmpresaModel(
                new EmpresaId(empresaEntity.idEmpresa()),
                new EmpresaName(empresaEntity.nameEmpresa()),
                new EmpresaIncorporationDate(LocalDate.parse(empresaEntity.incorporationDate())),
                new EmpresaAnnualBilling(new BigDecimal(empresaEntity.annualBilling())),
                new EmpresaSede(empresaEntity.sedeName(), empresaEntity.sedeDescription()),
                new EmpresaSector(empresaEntity.sectorName(), empresaEntity.sectorDescription())
        );
    }

    public EmpresaModel fromResultSetToModel(final ResultSet resultSet) throws SQLException {
        return fromEntityToModel(fromResultSetToEntity(resultSet));
    }

    public List<EmpresaModel> fromResultSetToModelList(final ResultSet resultSet) throws SQLException {
        final List<EmpresaModel> empresas = new ArrayList<>();
        while (resultSet.next()) {
            empresas.add(fromResultSetToModel(resultSet));
        }
        return empresas;
    }

}
