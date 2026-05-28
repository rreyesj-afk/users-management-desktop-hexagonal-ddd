package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record ChangeEmpresaSectorRequest(
    String idEmpresa,
    String sectorName,
    String sectorDescription
) {}