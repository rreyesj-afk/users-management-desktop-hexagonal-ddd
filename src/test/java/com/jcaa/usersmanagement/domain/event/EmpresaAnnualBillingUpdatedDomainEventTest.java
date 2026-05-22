package com.jcaa.usersmanagement.domain.event;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmpresaAnnualBillingUpdatedDomainEventTest {

    private static final String ID_EMPRESA = UUID.randomUUID().toString();
    private static final String EMPRESA_NAME = "Fortuna";
    private static final LocalDate EMPRESA_INCORPORATION_DATE = LocalDate.of(2025, 4, 8);
    private static final String EMPRESA_ANNUAL_BILLING = "20000000";

    private EmpresaModel empresa;

    @BeforeEach
    void setUp() {
        empresa = EmpresaModel.create(
                        new EmpresaId(ID_EMPRESA),
                        new EmpresaName(EMPRESA_NAME),
                        new EmpresaIncorporationDate(EMPRESA_INCORPORATION_DATE),
                        new EmpresaAnnualBilling(new BigDecimal(EMPRESA_ANNUAL_BILLING)),
                        new EmpresaSede("Fouver", "Enfocada en software."),
                        new EmpresaSector("Tecnologia", "Software")
                );
    }

    @Test
    @DisplayName("eventName() debe retornar empresa.annualbilling.updated")
    void shouldReturnCorrectEventName() {
        // Arrage
        final EmpresaAnnualBillingUpdatedDomainEvent event = new EmpresaAnnualBillingUpdatedDomainEvent(empresa);
        //Act
        final String result = event.getEventName();
        // Assert
        assertEquals("empresa.annualbilling.updated", result);
    }

    @Test
    @DisplayName("payload() debe contener los campos de annual billing")
    void shouldReturnPayloadWithAnnualBillingFields() {
        // Arrange
        final EmpresaAnnualBillingUpdatedDomainEvent event = new EmpresaAnnualBillingUpdatedDomainEvent(empresa);
        // Act
        final Map<String, String> payload = event.payload();
        // Assert
        assertAll("payload annual billing",
                () -> assertEquals(2, payload.size()),
                () -> assertEquals(ID_EMPRESA, payload.get("idEmpresa")),
                () -> assertEquals(EMPRESA_ANNUAL_BILLING, payload.get("annualBilling"))
        );
    }
}