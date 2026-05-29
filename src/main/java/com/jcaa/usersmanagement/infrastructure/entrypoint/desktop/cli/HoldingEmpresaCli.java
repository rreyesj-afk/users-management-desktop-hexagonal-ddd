package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.*;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.EmpresaResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.EmpresaMenuOption;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.EmpresaController;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public final class HoldingEmpresaCli {

    private static final String MENU_BORDER = "  ==========================================";

    private final EmpresaController empresaController;
    private final ConsoleIO empresaConsole;

    public void start() {
        final EmpresaResponsePrinter printer = new EmpresaResponsePrinter(empresaConsole);
        runLoop(buildHandlers(printer));
    }

    private void runLoop(final Map<EmpresaMenuOption, OperationHandler> handlers) {
        boolean running = true;
        while (running) {
            printMenu();
            final int choice = empresaConsole.readInt("\n  Option: ");
            final Optional<EmpresaMenuOption> option = EmpresaMenuOption.fromNumber(choice);

            if (option.isEmpty()) {
                empresaConsole.println("  Invalid option. Please try again.");
            } else if (option.get() == EmpresaMenuOption.EXIT) {
                running = false;
            } else {
                executeHandler(handlers, option.get()
                );
            }
        }
    }

    private void executeHandler(
            final Map<EmpresaMenuOption, OperationHandler> handlers, final EmpresaMenuOption option) {
        try {
            handlers.get(option).handle();
        } catch (final ConstraintViolationException exception) {
            empresaConsole.println("  Validation errors:");
            exception.getConstraintViolations()
                    .forEach(violation -> empresaConsole.println("    - " + violation.getMessage()));
        } catch (final RuntimeException exception) {
            empresaConsole.println("  Unexpected error: " + exception.getMessage());
        }
    }

    private Map<EmpresaMenuOption, OperationHandler> buildHandlers(final EmpresaResponsePrinter printer) {
        return Map.of(
                EmpresaMenuOption.LIST_EMPRESAS,            new ListEmpresasHandler(empresaController, printer),
                EmpresaMenuOption.FIND_EMPRESA,             new FindEmpresaByIdHandler(empresaController, empresaConsole, printer),
                EmpresaMenuOption.CREATE_EMPRESA,           new CreateEmpresaHandler(empresaController, empresaConsole, printer),
                EmpresaMenuOption.UPDATE_ANNUAL_BILLING,    new UpdateEmpresaAnnualBillingHandler(empresaController, empresaConsole, printer),
                EmpresaMenuOption.CHANGE_SEDE,              new ChangeEmpresaSedeHandler(empresaController, empresaConsole, printer),
                EmpresaMenuOption.CHANGE_SECTOR,            new ChangeEmpresaSectorHandler(empresaController, empresaConsole, printer),
                EmpresaMenuOption.DELETE_EMPRESA,           new DeleteEmpresaHandler(empresaController, empresaConsole)
        );
    }

    private void printMenu() {
        empresaConsole.println();
        empresaConsole.println(MENU_BORDER);
        empresaConsole.println("    Empresa Menu");
        empresaConsole.println(MENU_BORDER);

        for (final EmpresaMenuOption option : EmpresaMenuOption.values()) {
            empresaConsole.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
        }
        empresaConsole.println(MENU_BORDER);
    }
}