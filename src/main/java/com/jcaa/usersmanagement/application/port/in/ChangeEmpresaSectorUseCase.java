package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSectorCommand;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface ChangeEmpresaSectorUseCase {
    EmpresaModel execute(@NotNull @Valid ChangeEmpresaSectorCommand command);
}