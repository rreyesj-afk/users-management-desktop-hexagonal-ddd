package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSedeCommand;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface ChangeEmpresaSedeUseCase {
    EmpresaModel execute(@NotNull @Valid ChangeEmpresaSedeCommand command);
}