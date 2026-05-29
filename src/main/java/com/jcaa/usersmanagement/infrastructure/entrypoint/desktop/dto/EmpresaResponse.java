package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record EmpresaResponse(
    String idEmpresa,
    String nameEmpresa,
    String incorporationDate,
    String annualBilling,
    String sedeName,
    String sedeDescription,
    String sectorName,
    String sectorDescription
) {}