package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.ChangeEmpresaSectorUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSectorCommand;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ChangeEmpresaSectorService implements ChangeEmpresaSectorUseCase {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final EmpresaNotificationsService empresaNotificationsService;
    private final Validator validator;

    @Override
    public EmpresaModel execute(final ChangeEmpresaSectorCommand command) {

        validateCommand(command);

        final EmpresaId idEmpresa = new EmpresaId(command.idEmpresa());
        final EmpresaModel current = findfindExistingEmpresaOrFail(idEmpresa);
        final EmpresaSector newSector = new EmpresaSector(command.sectorName(), command.sectorDescription());
        final EmpresaModel empresaToUpdate = current.changeSector(newSector);
        final EmpresaModel updatedSector = empresaRepositoryPort.save(empresaToUpdate);

        empresaNotificationsService.notifySedeChanged(updatedSector);

        return empresaRepositoryPort.save(updatedSector);
    }

    private void validateCommand(final ChangeEmpresaSectorCommand command) {
        final Set<ConstraintViolation<ChangeEmpresaSectorCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private EmpresaModel findfindExistingEmpresaOrFail(final EmpresaId idEmpresa) {
        return empresaRepositoryPort.findById(idEmpresa).orElseThrow(
                () -> EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.value())
        );
    }

}