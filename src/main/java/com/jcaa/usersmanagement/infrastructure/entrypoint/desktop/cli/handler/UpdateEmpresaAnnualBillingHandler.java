package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.exception.InvalidEmpresaAnnualBillingException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateEmpresaAnnualBillingRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateEmpresaAnnualBillingHandler implements OperationHandler {

    private final EmpresaController empresaController;
    private final ConsoleIO empresaConsole;
    private final EmpresaResponsePrinter  empresaResponsePrinter;

    @Override
    public void handle() {
        final String empresa_id     = empresaConsole.readRequired("Empresa ID          :");
        final String annual_billing = empresaConsole.readRequired("Annual Billing      :");

        try {
            final EmpresaResponse annualBillingUpdated = empresaController.updateAnnualBilling(
                    new UpdateEmpresaAnnualBillingRequest(
                            empresa_id,
                            annual_billing));
            empresaConsole.println("\n  Annual billign update successfully.");
            empresaResponsePrinter.print(annualBillingUpdated);
        }

        catch (final InvalidEmpresaAnnualBillingException | EmpresaNotFoundException exception) {
            empresaConsole.println("Error: " + exception.getMessage()
            );
        }
    }
}
