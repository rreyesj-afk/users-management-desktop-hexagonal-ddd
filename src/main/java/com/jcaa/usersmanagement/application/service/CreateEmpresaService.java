package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.CreateEmpresaUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmpresaCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmpresaApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmpresaAlreadyExistsException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Set;

@Log
@RequiredArgsConstructor
public final class CreateEmpresaService implements CreateEmpresaUseCase {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final EmpresaNotificationsService empresaNotificationService;
    private final Validator validator;

    @Override
    public EmpresaModel execute(final CreateEmpresaCommand command) {

        validateCommand(command);

        final EmpresaName nameEmpresa = new EmpresaName(command.nameEmpresa());
        ensureEmpresaNameIsNotTaken(nameEmpresa);

        final EmpresaModel empresaToSave = EmpresaApplicationMapper.fromCreateCommandToModel(command);
        final EmpresaModel savedEmpresa = empresaRepositoryPort.saveEmpresa(empresaToSave);

        empresaNotificationService.notifyEmpresaCreated(savedEmpresa);

        return savedEmpresa;
    }

    private void validateCommand(final CreateEmpresaCommand command) {
        final Set<ConstraintViolation<CreateEmpresaCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private void ensureEmpresaNameIsNotTaken (final EmpresaName nameEmpresa) {
        empresaRepositoryPort.findEmpresaByName(nameEmpresa).ifPresent(ignored -> {
            throw EmpresaAlreadyExistsException.becauseNameAlreadyExists(nameEmpresa.value());
        });
    }
}
