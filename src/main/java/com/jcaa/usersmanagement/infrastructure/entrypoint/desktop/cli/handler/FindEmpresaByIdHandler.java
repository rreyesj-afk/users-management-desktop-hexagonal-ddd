package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindEmpresaByIdHandler implements OperationHandler {

    private final EmpresaController empresaController;
    private final ConsoleIO empresaConsole;
    private final EmpresaResponsePrinter  empresaResponsePrinter;

    @Override
    public void handle() {
        final String empresa_id = empresaConsole.readRequired("Empresa ID: ");
        try {
            final EmpresaResponse empresa = empresaController.findEmpresaById(empresa_id);
            empresaResponsePrinter.print(empresa);
        }

        catch (final EmpresaNotFoundException exception) {
            empresaConsole.println("  Not found: " + exception.getMessage());
        }
    }
}
