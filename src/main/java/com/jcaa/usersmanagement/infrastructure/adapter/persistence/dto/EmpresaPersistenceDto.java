package com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto;

public record EmpresaPersistenceDto(
        String idEmpresa,
        String nameEmpresa,
        String incorporationDate,
        String annualBilling,
        String sedeName,
        String sedeDescription,
        String sectorName,
        String sectorDescription,
        String createdAt,
        String updatedAt
) {}

