package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.EmpresaResponse;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class EmpresaResponsePrinter {

    private static final String SEPARATOR = "-".repeat(52);
    private static final String ROW_FORMAT = "  %-10s : %s%n";

    private final ConsoleIO console;

    public void print(final EmpresaResponse empresaResponse) {
        console.println(SEPARATOR);
        console.printf(ROW_FORMAT, "Empresa Id",            empresaResponse.idEmpresa());
        console.printf(ROW_FORMAT, "Empresa Name",          empresaResponse.nameEmpresa());
        console.printf(ROW_FORMAT, "Incorporation Date",    empresaResponse.incorporationDate());
        console.printf(ROW_FORMAT, "Annual Billing",        empresaResponse.annualBilling());
        console.printf(ROW_FORMAT, "Sede Name",             empresaResponse.sedeName());
        console.printf(ROW_FORMAT, "Sede Description",      empresaResponse.sedeDescription());
        console.printf(ROW_FORMAT, "Sector Name",           empresaResponse.sectorName());
        console.printf(ROW_FORMAT, "Sector Description",    empresaResponse.sectorDescription());
        console.println(SEPARATOR);
    }

    public void printList(final List<EmpresaResponse> empresas) {
        if (empresas.isEmpty()) {
            console.println("No empresas found.");
            return;
        }
        console.printf("%n Total: %d empresa(s)%n", empresas.size());
        empresas.forEach(this::print);
    }
}
