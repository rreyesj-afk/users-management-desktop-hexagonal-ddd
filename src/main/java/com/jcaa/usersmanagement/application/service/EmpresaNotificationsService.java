package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public final class EmpresaNotificationsService {

    public void notifyEmpresaCreated(final EmpresaModel empresa) {
        log.info(String.format("[EMPRESA CREATED] Empresa '%s' creada correctamente. ID: %s",
                empresa.getNameEmpresa().value(),
                empresa.getIdEmpresa().value())
        );
    }

    public void notifyAnnualBillingUpdated(final EmpresaModel empresa) {

        log.info(String.format("[EMPRESA UPDATED] Facturación actualizada para '%s'. Nuevo valor: %s",
                empresa.getNameEmpresa().value(),
                empresa.getAnnualBilling().value())
        );
    }

    public void notifySedeChanged(final EmpresaModel empresa) {

        log.info(String.format("[EMPRESA UPDATED] Sede actualizada para '%s'. Nueva sede: %s",
                empresa.getNameEmpresa().value(),
                empresa.getSede().sedeName())
        );
    }

    public void notifySectorChanged(final EmpresaModel empresa) {

        log.info(String.format("[EMPRESA UPDATED] Sector actualizado para '%s'. Nuevo sector: %s",
                empresa.getNameEmpresa().value(),
                empresa.getSector().sectorName())
        );
    }

    public void notifyEmpresaDeleted(final EmpresaModel empresa) {
        log.info(String.format("[EMPRESA DELETED] Empresa '%s' eliminada. ID: %s",
                empresa.getNameEmpresa().value(),
                empresa.getIdEmpresa().value())
        );
    }
}