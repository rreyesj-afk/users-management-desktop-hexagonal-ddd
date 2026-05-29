package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import java.util.Map;
import lombok.Getter;

@Getter
public final class EmpresaCreatedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "empresa.created";

    private final EmpresaModel empresa;

    public EmpresaCreatedDomainEvent(final EmpresaModel empresa) {
        super(EVENT_NAME);
        this.empresa = empresa;
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "idEmpresa", empresa.getIdEmpresa().value(),
                "nameEmpresa", empresa.getNameEmpresa().value(),
                "incorporationDate", empresa.getIncorporationDate().toString(),
                "annualBilling", empresa.getAnnualBilling().value().toString(),
                "sedeName", empresa.getSede().sedeName(),
                "sedeDescription", empresa.getSede().sedeDescription(),
                "sectorName", empresa.getSector().sectorName(),
                "sectorDescription", empresa.getSector().sectorDescription()
        );
    }
}
