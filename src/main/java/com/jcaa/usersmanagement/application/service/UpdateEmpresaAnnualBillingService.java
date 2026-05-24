package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateEmpresaAnnualBillingUseCase;
import com.jcaa.usersmanagement.application.port.out.EmpresaRepositoryPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmpresaAnnualBillingCommand;
import com.jcaa.usersmanagement.application.service.mapper.EmpresaApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateEmpresaAnnualBillingService implements UpdateEmpresaAnnualBillingUseCase {

    private final EmpresaRepositoryPort empresaRepositoryPort;
    private final EmpresaNotificationsService empresaNotificationsService;
    private final Validator validator;

    @Override
    public EmpresaModel execute(
            final UpdateEmpresaAnnualBillingCommand command) {

        validateCommand(command);

        final EmpresaId idEmpresa = EmpresaApplicationMapper.fromUpdateAnnualBillingCommandToEmpresaId(command);
        final EmpresaModel current = findExistingEmpresaOrFail(idEmpresa);
        final EmpresaAnnualBilling newAnnualBilling = EmpresaApplicationMapper.fromUpdateAnnualBillingCommandToAnnualBilling(command);
        final EmpresaModel empresaToUpdate = current.updateAnnualBilling(newAnnualBilling);
        final EmpresaModel updatedEmpresa = empresaRepositoryPort.save(empresaToUpdate);

        empresaNotificationsService.notifyAnnualBillingUpdated(updatedEmpresa);

        return updatedEmpresa;
    }

    private void validateCommand(final UpdateEmpresaAnnualBillingCommand command) {
        final Set<ConstraintViolation<UpdateEmpresaAnnualBillingCommand>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private EmpresaModel findExistingEmpresaOrFail(final EmpresaId idEmpresa) {
        return  empresaRepositoryPort.findById(idEmpresa).orElseThrow(
                () -> EmpresaNotFoundException.becauseIdWasNotFound(idEmpresa.value())
        );
    }
}