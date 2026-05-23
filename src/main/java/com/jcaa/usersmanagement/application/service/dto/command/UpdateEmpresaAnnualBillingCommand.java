package com.jcaa.usersmanagement.application.service.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateEmpresaAnnualBillingCommand(

        @NotBlank(message = "empresa id must not be blank")
            String empresaId,
        @NotNull(message = "annual billing must not be null")
            @Positive(message = "annual billing must be greater than zero")
            BigDecimal annualBilling
)
{}