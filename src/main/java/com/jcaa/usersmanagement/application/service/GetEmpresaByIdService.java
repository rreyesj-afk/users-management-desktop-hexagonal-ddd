package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetEmpresaByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.EmpresaApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class GetEmpresaByIdService implements GetEmpresaByIdUseCase {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final Validator validator;

    @Override
    public EmpresaModel execute(final GetEmpresaByIdQuery query) {
        validateQuery(query);

        final EmpresaId idEmpresa = EmpresaApplicationMapper.fromGetEmpresaByIdQueryToEmpresaId(query);
        return empresaRepositoryPort
                .findEmpresaById(idEmpresa)
                .orElseThrow(() -> EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.value()));
    }

    private void validateQuery(final GetEmpresaByIdQuery query) {
        final Set<ConstraintViolation<GetEmpresaByIdQuery>> violations = validator.validate(query);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
