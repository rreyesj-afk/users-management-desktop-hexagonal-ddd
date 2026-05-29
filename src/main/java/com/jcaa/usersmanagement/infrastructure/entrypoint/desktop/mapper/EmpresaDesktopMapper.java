package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.*;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.*;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class EmpresaDesktopMapper {

    public CreateEmpresaCommand toCreateCommand(final CreateEmpresaRequest request) {

        return new CreateEmpresaCommand(
                request.nameEmpresa(),
                LocalDate.parse(request.incorporationDate()),
                new BigDecimal(request.annualBilling()),
                request.sedeName(),
                request.sedeDescription(),
                request.sectorName(),
                request.sectorDescription()
        );
    }

    public UpdateEmpresaAnnualBillingCommand toUpdateAnnualBillingCommand(final UpdateEmpresaAnnualBillingRequest request) {
        return new UpdateEmpresaAnnualBillingCommand(request.idEmpresa(),
                new BigDecimal(request.annualBilling())
        );
    }

    public ChangeEmpresaSedeCommand toChangeSedeCommand(final ChangeEmpresaSedeRequest request) {
        return new ChangeEmpresaSedeCommand(
                request.idEmpresa(),
                request.sedeName(),
                request.sedeDescription()
        );
    }

    public ChangeEmpresaSectorCommand toChangeSectorCommand(final ChangeEmpresaSectorRequest request) {
        return new ChangeEmpresaSectorCommand(
                request.idEmpresa(),
                request.sectorName(),
                request.sectorDescription()
        );
    }

    public DeleteEmpresaCommand toDeleteCommand(final String idEmpresa) {
        return new DeleteEmpresaCommand(idEmpresa);
    }

    public GetEmpresaByIdQuery toGetByIdQuery(final String idEmpresa) {
        return new GetEmpresaByIdQuery(idEmpresa);
    }

    public EmpresaResponse toResponse(final EmpresaModel empresa) {
        final String formattedId = String.format("EMP-%04d", Integer.parseInt(empresa.getIdEmpresa().value()));
        return new EmpresaResponse(
                formattedId,
                empresa.getNameEmpresa().value(),
                empresa.getIncorporationDate().value().toString(),
                empresa.getAnnualBilling().value().toString(),
                empresa.getSede().sedeName(),
                empresa.getSede().sedeDescription(),
                empresa.getSector().sectorName(),
                empresa.getSector().sectorDescription()
        );
    }

    public List<EmpresaResponse> toResponseList(final List<EmpresaModel> empresas) {
        return empresas.stream().map(EmpresaDesktopMapper::toResponse).toList();
    }
}