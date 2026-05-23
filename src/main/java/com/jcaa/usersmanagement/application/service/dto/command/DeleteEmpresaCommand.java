package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;

public record DeleteEmpresaCommand(
        @NotBlank(message = "idempresa must not be blank") String empresaId)
{}