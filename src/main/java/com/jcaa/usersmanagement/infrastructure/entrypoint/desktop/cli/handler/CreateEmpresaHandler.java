package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmpresaAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateEmpresaRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CreateEmpresaHandler implements OperationHandler{

    private final EmpresaController empresaController;
    private final ConsoleIO empresaConsole;
    private final EmpresaResponsePrinter empresaResponsePrinter;

    @Override
    public void handle() {
        final String empresa_name           = empresaConsole.readRequired("Empresa Name         :");
        final String incorporacion_date     = empresaConsole.readRequired("Incorporacion Date   :");
        final String annual_billing         = empresaConsole.readRequired("Annual Billing      :");
        final String sede_name              = empresaConsole.readRequired("Sede Name            :");
        final String sede_description       = empresaConsole.readRequired("Sede Description     :");
        final String sector_name            = empresaConsole.readRequired("Sector Name          :");
        final String sector_description     = empresaConsole.readRequired("Sector Description  :");

        try {
            final EmpresaResponse empresaCreated = empresaController.createEmpresa(
                    new CreateEmpresaRequest(
                            empresa_name,
                            incorporacion_date,
                            annual_billing,
                            sede_name,
                            sede_description,
                            sector_name,
                            sector_description));
            empresaConsole.println("\n  Empresa created successfully.");
            empresaResponsePrinter.print(empresaCreated);
        }

        catch (final EmpresaAlreadyExistsException exception) {
            empresaConsole.println(("Error: " + exception.getMessage()));
        }
    }
}
