package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateEmpresaCommand(

        @NotBlank(message = "empresa name must not be blank")
                @Size(min = 5, message = "empresa name must have at least 5 characters")
                String nameEmpresa,
        @NotNull(message = "incorporation date must not be null")
                LocalDate incorporationDate,
        @NotNull(message = "annual billing must not be null")
                @Positive(message = "annual billing must be greater than zero")
                BigDecimal annualBilling,
        @NotBlank(message = "sede name must not be blank")
                @Size(min = 3, message = "sede name must have at least 3 characters")
                String sedeName,
        @NotBlank(message = "sede description must not be blank")
                String sedeDescription,
        @NotBlank(message = "sector name must not be blank")
                @Size(min = 3, message = "sector name must have at least 3 characters")
                String sectorName,
        @NotBlank(message = "sector description must not be blank")
                String sectorDescription

)
{}