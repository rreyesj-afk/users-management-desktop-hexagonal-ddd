package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.ChangeEmpresaSedeUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSedeCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmpresaApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ChangeEmpresaSedeService implements ChangeEmpresaSedeUseCase {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final EmpresaNotificationsService empresaNotificationsService;
    private final Validator validator;

    @Override
    public EmpresaModel execute(final ChangeEmpresaSedeCommand command) {

        validateCommand(command);

        final EmpresaId idEmpresa = EmpresaApplicationMapper.fromChangeSedeCommandToEmpresaId(command);
        final EmpresaModel current = findExistingEmpresaOrFail(idEmpresa);
        final EmpresaSede newSede = EmpresaApplicationMapper.fromChangeSedeCommandToSede(command);
        final EmpresaModel empresaToUpdate = current.changeSede(newSede);
        final EmpresaModel changeSede = empresaRepositoryPort.changeEmpresaSede(empresaToUpdate);

        empresaNotificationsService.notifySedeChanged(changeSede);

        return changeSede;
    }

    private void validateCommand(final ChangeEmpresaSedeCommand command) {
        final Set<ConstraintViolation<ChangeEmpresaSedeCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private EmpresaModel findExistingEmpresaOrFail(final EmpresaId idEmpresa) {
        return empresaRepositoryPort.findEmpresaById(idEmpresa).orElseThrow(
                () -> EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.value())
        );
    }

}