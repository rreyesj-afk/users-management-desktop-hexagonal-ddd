package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record UpdateEmpresaAnnualBillingRequest(
    String idEmpresa,
    String annualBilling
) {}