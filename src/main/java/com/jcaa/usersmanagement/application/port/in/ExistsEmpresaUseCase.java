package com.jcaa.usersmanagement.application.port.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public interface ExistsEmpresaUseCase {
    boolean execute(@NotBlank @Valid String idEmpresa);
}
