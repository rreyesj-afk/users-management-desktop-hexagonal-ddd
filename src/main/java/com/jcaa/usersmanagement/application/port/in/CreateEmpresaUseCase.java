package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmpresaCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface CreateEmpresaUseCase {
    EmpresaModel execute(@NotNull @Valid CreateEmpresaCommand command);
}
