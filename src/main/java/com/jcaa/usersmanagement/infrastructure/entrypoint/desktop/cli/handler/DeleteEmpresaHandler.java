package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteEmpresaHandler implements OperationHandler{

    private final EmpresaController empresaController;
    private final ConsoleIO empresaConsole;
    @Override
    public void handle() {
        final String empresa_id = empresaConsole.readRequired("Empresa ID to delete: ");
        try {
            empresaController.deleteEmpresa(empresa_id);
            empresaConsole.println("Empresa has been deleted");
        }
        catch (final EmpresaNotFoundException exception) {
            empresaConsole.println("Empresa not found: " + exception.getMessage());
        }
    }
}
