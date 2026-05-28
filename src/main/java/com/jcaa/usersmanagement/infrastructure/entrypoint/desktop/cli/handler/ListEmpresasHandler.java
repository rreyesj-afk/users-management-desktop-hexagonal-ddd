package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ListEmpresasHandler implements OperationHandler {

    private final EmpresaController empresaController;
    private final EmpresaResponsePrinter empresaResponsePrinter;

    @Override
    public void handle() {
        final List<EmpresaResponse> empresas = empresaController.listAllEmpresas();
        empresaResponsePrinter.printList(empresas);
    }
}