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
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmpresaCreatedDomainEventTest {

    private static final String ID_EMPRESA = "1";
    private static final String EMPRESA_NAME = "Fortuna";
    private static final LocalDate EMPRESA_INCORPORATION_DATE = LocalDate.of(2025, 4, 8);
    private static final BigDecimal EMPRESA_ANNUAL_BILLING = new BigDecimal("10000000");
    private static final String EMPRESA_SEDE_NAME = "Fouver";
    private static final String EMPRESA_SEDE_DESCRIPTION = "Enfocada en software.";
    private static final String EMPRESA_SECTOR_NAME = "Tecnologia";
    private static final String EMPRESA_SECTOR_DESCRIPTION = "Desarrollo de software.";

    private EmpresaModel empresa;

    @BeforeEach
    void setUp() {
        empresa = new EmpresaModel(
                        new EmpresaId(ID_EMPRESA),
                        new EmpresaName(EMPRESA_NAME),
                        new EmpresaIncorporationDate(EMPRESA_INCORPORATION_DATE),
                        new EmpresaAnnualBilling(EMPRESA_ANNUAL_BILLING),
                        new EmpresaSede(EMPRESA_SEDE_NAME, EMPRESA_SEDE_DESCRIPTION),
                        new EmpresaSector(EMPRESA_SECTOR_NAME, EMPRESA_SECTOR_DESCRIPTION)
                );
    }

    //eventName

    @Test
    @DisplayName("eventName() deber retornar la constante 'empresa.created'")
    void shouldCreateEventWithCorrectName() {
        // Arrage
        final EmpresaCreatedDomainEvent event = new EmpresaCreatedDomainEvent(empresa);
        //Act
        final String result = event.getEventName();
        // Assert
        assertEquals("empresa.created", result);
    }

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final EmpresaCreatedDomainEvent event = new EmpresaCreatedDomainEvent(empresa);
        final LocalDateTime after = LocalDateTime.now();
        // Act
        final LocalDateTime occurredOn = event.getOccurredOn();
        // Assert
        assertNotNull(occurredOn, "occurredOn no debe ser null");
        assertFalse(occurredOn.isBefore(before), "occurredOn debe ser >= al instante anterior a la construcción");
        assertFalse(occurredOn.isAfter(after), "occurredOn debe ser <= al instante posterior a la construcción");
    }

    @Test
    @DisplayName("empresa() debe devolver la misma instancia de EmpresaModel")
    void shouldReturnSameEmpresaInstance() {
        // Arrange
        final EmpresaCreatedDomainEvent event = new EmpresaCreatedDomainEvent(empresa);
        // Act
        final EmpresaModel result = event.getEmpresa();
        // Assert
        assertSame(empresa, result);
    }

    @Test
    @DisplayName("payload() debe contener exactamente campos de empresa")
    void shouldReturnPayloadWithAllEmpresaFields() {
        // Arrange
        final EmpresaCreatedDomainEvent event = new EmpresaCreatedDomainEvent(empresa);
        // Act
        final Map<String, String> payload = event.payload();
        // Assert
        assertAll("payload de EmpresaCreatedDomainEvent",
                () -> assertEquals(8, payload.size(), "tamaño del mapa"),
                () -> assertEquals(ID_EMPRESA, payload.get("idEmpresa"), "empresa id"),
                () -> assertEquals(EMPRESA_NAME, payload.get("nameEmpresa"), "empresa name"),
                () -> assertEquals(EMPRESA_INCORPORATION_DATE.toString(), payload.get("incorporationDate"), "incorporation date"),
                () -> assertEquals(EMPRESA_ANNUAL_BILLING.toString(), payload.get("annualBilling"), "annual billing"),
                () -> assertEquals(EMPRESA_SEDE_NAME, payload.get("sedeName"), "sede name"),
                () -> assertEquals(EMPRESA_SEDE_DESCRIPTION, payload.get("sedeDescription"), "sede description"),
                () -> assertEquals(EMPRESA_SECTOR_NAME, payload.get("sectorName"), "sector name"),
                () -> assertEquals(EMPRESA_SECTOR_DESCRIPTION, payload.get("sectorDescription"), "sector description"));
    }

}
