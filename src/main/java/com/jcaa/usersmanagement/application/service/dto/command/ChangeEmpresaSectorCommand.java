package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmpresaSectorCommand(

        @NotBlank(message = "empresa id must not be blank")
            String empresaId,
        @NotBlank(message = "sector name must not be blank")
            @Size(min = 3, message = "sector name must have at least 3 characters")
            String sectorName,
        @NotBlank(message = "sector description must not be blank")
            String sectorDescription
)
{}