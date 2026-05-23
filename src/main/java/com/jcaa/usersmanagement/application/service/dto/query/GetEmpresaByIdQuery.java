package com.jcaa.usersmanagement.application.service.dto.query;

import jakarta.validation.constraints.NotBlank;

public record GetEmpresaByIdQuery(@NotBlank(message = "empresa id must not be blank") String empresaId)
{}