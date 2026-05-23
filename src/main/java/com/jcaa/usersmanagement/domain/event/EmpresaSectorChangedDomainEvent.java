package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import java.util.Map;
import lombok.Getter;

@Getter
public final class EmpresaSectorChangedDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "empresa.sector.changed";

    private final EmpresaModel empresa;

    public EmpresaSectorChangedDomainEvent(final EmpresaModel empresa) {
        super(EVENT_NAME);
        this.empresa = empresa;
    }

    @Override
    public Map<String, String> payload() {
        return Map.of(
                "idEmpresa", empresa.getIdEmpresa().value(),
                "sectorName", empresa.getSector().sectorName(),
                "sectorDescription", empresa.getSector().sectorDescription()
        );
    }
}