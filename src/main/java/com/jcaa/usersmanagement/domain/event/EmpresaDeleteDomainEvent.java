package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import java.util.Map;
import lombok.Getter;

@Getter
public final class EmpresaDeleteDomainEvent extends DomainEvent {

    private static final String EVENT_NAME = "empresa.deleted";

    private final EmpresaId idEmpresa;

    public EmpresaDeleteDomainEvent(final EmpresaId idEmpresa) {
        super(EVENT_NAME);
        this.idEmpresa = idEmpresa;
    }

    @Override
    public Map<String, String> payload() {
        return Map.of("id", idEmpresa.value());
    }
}
