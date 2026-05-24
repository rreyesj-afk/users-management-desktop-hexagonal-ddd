package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmpresaSedeCommand(

        @NotBlank(message = "empresa id must not be blank")
                String idEmpresa,
        @NotBlank(message = "sede name must not be blank")
                @Size(min = 3, message = "sede name must have at least 3 characters")
                String sedeName,
        @NotBlank(message = "sede description must not be blank")
                String sedeDescription
)
{}