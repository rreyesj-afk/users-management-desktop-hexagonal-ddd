package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllEmpresasUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class GetAllEmpresasService implements GetAllEmpresasUseCase {

    private final EmpresaRepositoryPort getAllEmpresasPort;

    @Override
    public List<EmpresaModel> execute() {
        return getAllEmpresasPort.findAll();
    }
}
