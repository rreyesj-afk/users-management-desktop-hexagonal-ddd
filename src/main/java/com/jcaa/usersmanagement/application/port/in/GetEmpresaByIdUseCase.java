package com.jcaa.usersmanagement.application.port.in;

import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public interface GetEmpresaByIdUseCase {
    EmpresaModel execute(@NotNull @Valid GetEmpresaByIdQuery command);
}
