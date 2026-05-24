package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSectorCommand;
import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSedeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmpresaCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmpresaCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmpresaAnnualBillingCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmpresaApplicationMapper {

    public EmpresaModel fromCreateCommandToModel(final CreateEmpresaCommand command) {

        return EmpresaModel.create(
                EmpresaId.newId(),
                new EmpresaName(command.nameEmpresa()),
                new EmpresaIncorporationDate(command.incorporationDate()),
                new EmpresaAnnualBilling(command.annualBilling()),
                new EmpresaSede(
                        command.sedeName(),
                        command.sedeDescription()),
                new EmpresaSector(
                        command.sectorName(),
                        command.sectorDescription()));
    }

    public EmpresaId fromUpdateAnnualBillingCommandToEmpresaId(final UpdateEmpresaAnnualBillingCommand command) {
        return new EmpresaId(command.idEmpresa());
    }
        public EmpresaAnnualBilling fromUpdateAnnualBillingCommandToAnnualBilling(
                final UpdateEmpresaAnnualBillingCommand command) {
        return new EmpresaAnnualBilling(
                command.annualBilling()
            );
        }

    public EmpresaId fromChangeSedeCommandToEmpresaId(final ChangeEmpresaSedeCommand command) {
        return new EmpresaId(command.idEmpresa());
    }

    public EmpresaSede fromChangeSedeCommandToSede(final ChangeEmpresaSedeCommand command) {
        return new EmpresaSede(
                command.sedeName(),
                command.sedeDescription()
        );
    }

    public EmpresaId fromChangeSectorCommandToEmpresaId(final ChangeEmpresaSectorCommand command) {
        return new EmpresaId(command.idEmpresa());
    }

    public EmpresaSector fromChangeSectorCommandToSector(final ChangeEmpresaSectorCommand command) {
        return new EmpresaSector(
                command.sectorName(),
                command.sectorDescription()
        );
    }

    public EmpresaId fromGetEmpresaByIdQueryToEmpresaId(final GetEmpresaByIdQuery query) {
        return new EmpresaId(query.empresaId());
    }

    public EmpresaId fromDeleteCommandToEmpresaId(final DeleteEmpresaCommand command) {
        return new EmpresaId(command.empresaId());
    }
}