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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmpresaSedeChangedDomainEvent")
class EmpresaSedeChangedDomainEventTest {

    private static final String ID_EMPRESA = "0001";
    private static final String SEDE_NAME = "Fouver";
    private static final String SEDE_DESCRIPTION = "Enfocada en software.";

    private EmpresaModel empresa;

    @BeforeEach
    void setUp() {
        empresa = EmpresaModel.create(
                new EmpresaId("0001"),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede(SEDE_NAME, SEDE_DESCRIPTION),
                new EmpresaSector("Tecnologia", "Software")
        );
    }

    @Test
    @DisplayName("eventName() debe retornar empresa.sede.changed")
    void shouldReturnCorrectEventName() {
        // Arrage
        final EmpresaSedeChangedDomainEvent event = new EmpresaSedeChangedDomainEvent(empresa);
        //Act
        final String result = event.getEventName();
        // Assert
        assertEquals("empresa.sector.changed", result);
    }

    @Test
    @DisplayName("payload() debe contener campos de sede")
    void shouldReturnPayloadWithSedeFields() {
        // Arrange
        final EmpresaSedeChangedDomainEvent event = new EmpresaSedeChangedDomainEvent(empresa);
        // Act
        final Map<String, String> payload = event.payload();
        // Assert
        assertAll("payload sede",
                () -> assertEquals(3, payload.size()),
                () -> assertEquals(ID_EMPRESA, payload.get("idEmpresa")),
                () -> assertEquals(SEDE_NAME, payload.get("sedeName")),
                () -> assertEquals(SEDE_DESCRIPTION, payload.get("sedeDescription"))
        );
    }
}