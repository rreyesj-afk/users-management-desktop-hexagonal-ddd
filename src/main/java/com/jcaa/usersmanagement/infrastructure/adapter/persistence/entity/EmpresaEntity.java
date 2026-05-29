package com.jcaa.usersmanagement.infrastructure.adapter.persistence.entity;

public record EmpresaEntity(
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