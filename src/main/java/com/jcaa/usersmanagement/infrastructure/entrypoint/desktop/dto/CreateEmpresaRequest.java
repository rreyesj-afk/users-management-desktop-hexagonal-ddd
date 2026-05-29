package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record CreateEmpresaRequest(
    String nameEmpresa,
    String incorporationDate,
    String annualBilling,
    String sedeName,
    String sedeDescription,
    String sectorName,
    String sectorDescription
) {}