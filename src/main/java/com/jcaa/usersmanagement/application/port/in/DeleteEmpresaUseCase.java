package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmpresaCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface DeleteEmpresaUseCase {
    void execute(@NotNull @Valid DeleteEmpresaCommand command);
}
