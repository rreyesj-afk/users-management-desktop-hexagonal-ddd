package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateEmpresaUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateEmpresaAnnualBillingUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllEmpresasUseCase;
import com.jcaa.usersmanagement.application.port.in.GetEmpresaByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.ChangeEmpresaSedeUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteEmpresaUseCase;
import com.jcaa.usersmanagement.application.port.in.ChangeEmpresaSectorUseCase;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateEmpresaRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ChangeEmpresaSedeRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ChangeEmpresaSectorRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateEmpresaAnnualBillingRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.EmpresaDesktopMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class EmpresaController {

    private final CreateEmpresaUseCase createEmpresaUseCase;
    private final UpdateEmpresaAnnualBillingUseCase updateEmpresaAnnualBillingUseCase;
    private final ChangeEmpresaSedeUseCase changeEmpresaSedeUseCase;
    private final ChangeEmpresaSectorUseCase changeEmpresaSectorUseCase;
    private final GetEmpresaByIdUseCase getEmpresaByIdUseCase;
    private final GetAllEmpresasUseCase getAllEmpresasUseCase;
    private final DeleteEmpresaUseCase deleteEmpresaUseCase;

    public List<EmpresaResponse> listAllEmpresas() {
        final var empresas = getAllEmpresasUseCase.execute();
        return EmpresaDesktopMapper.toResponseList(empresas);
    }

    public EmpresaResponse findEmpresaById(final String id) {
        final var query = EmpresaDesktopMapper.toGetByIdQuery(id);
        final var empresa = getEmpresaByIdUseCase.execute(query);
        return EmpresaDesktopMapper.toResponse(empresa);
    }

    public EmpresaResponse createEmpresa(
            final CreateEmpresaRequest request) {
        final var command = EmpresaDesktopMapper.toCreateCommand(request);
        final var empresa = createEmpresaUseCase.execute(command);
        return EmpresaDesktopMapper.toResponse(empresa);
    }

    public EmpresaResponse updateAnnualBilling(
            final UpdateEmpresaAnnualBillingRequest request) {
        final var command = EmpresaDesktopMapper.toUpdateAnnualBillingCommand(request);
        final var empresa = updateEmpresaAnnualBillingUseCase.execute(command);
        return EmpresaDesktopMapper.toResponse(empresa);
    }

    public EmpresaResponse changeSede(
            final ChangeEmpresaSedeRequest request) {
        final var command = EmpresaDesktopMapper.toChangeSedeCommand(request);
        final var empresa = changeEmpresaSedeUseCase.execute(command);
        return EmpresaDesktopMapper.toResponse(empresa);
    }

    public EmpresaResponse changeSector(
            final ChangeEmpresaSectorRequest request) {
        final var command = EmpresaDesktopMapper.toChangeSectorCommand(request);
        final var empresa = changeEmpresaSectorUseCase.execute(command);
        return EmpresaDesktopMapper.toResponse(empresa);
    }

    public void deleteEmpresa(final String id) {
        final var command = EmpresaDesktopMapper.toDeleteCommand(id);
        deleteEmpresaUseCase.execute(command);
    }
}