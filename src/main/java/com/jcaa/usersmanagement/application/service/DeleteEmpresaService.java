package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteEmpresaUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmpresaCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmpresaApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class DeleteEmpresaService implements DeleteEmpresaUseCase {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final Validator validator;

    @Override
    public void execute(final DeleteEmpresaCommand command){
        validateCommand(command);

        final EmpresaId idEmpresa = EmpresaApplicationMapper.fromDeleteCommandToEmpresaId(command);
        ensureEmpresaExists(idEmpresa);
        empresaRepositoryPort.delete(idEmpresa);
    }

    private void validateCommand(final DeleteEmpresaCommand command){
        final Set<ConstraintViolation<DeleteEmpresaCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void ensureEmpresaExists(final EmpresaId idEmpresa) {
        empresaRepositoryPort.findById(idEmpresa).orElseThrow(() ->
            EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.toString())
        );
    }

}
