package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

public record ChangeEmpresaSedeRequest(
    String idEmpresa,
    String sedeName,
    String sedeDescription
) {}