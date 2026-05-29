package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmpresaAnnualBillingCommand;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface UpdateEmpresaAnnualBillingUseCase {
    EmpresaModel execute(@NotNull @Valid UpdateEmpresaAnnualBillingCommand command);
}