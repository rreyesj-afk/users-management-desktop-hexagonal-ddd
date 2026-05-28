package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaSedeException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.ChangeEmpresaSedeRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChangeEmpresaSedeHandler implements OperationHandler{

    private final EmpresaController empresaController;
    private final ConsoleIO empresaConsole;
    private final EmpresaResponsePrinter  empresaResponsePrinter;

    @Override
    public void handle() {
        final String empresa_id         = empresaConsole.readRequired("Empresa ID:        ");
        final String sede_name          = empresaConsole.readRequired("Sede Name:         ");
        final String sede_description   = empresaConsole.readRequired("Sede Description:  ");

        try {
            final EmpresaResponse empresaSedeChanged = empresaController.changeSede(
                    new ChangeEmpresaSedeRequest(
                            empresa_id,
                            sede_name,
                            sede_description
                    )
            );
            empresaConsole.println("\n Sede change successfully.");
            empresaResponsePrinter.print(empresaSedeChanged);
        }

        catch (final InvalidEmpresaSedeException | EmpresaNotFoundException exception) {
            empresaConsole.println("Error: " + exception.getMessage());
        }
    }
}
