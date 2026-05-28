package com.jcaa.usersmanagement.domain.event;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("UserDeletedDomainEvent")
class EmpresaDeleteDomainEventTest {

    private static final String EMPRESA_ID = "0001";

    // ── eventName

    @Test
    @DisplayName("eventName() debe retornar la constante 'empresa.deleted'")
    void shouldHaveEventNameEmpresaDeleted() {
        // Arrange
        final EmpresaDeleteDomainEvent event = new EmpresaDeleteDomainEvent(new EmpresaId(EMPRESA_ID));
        // Act
        final String result = event.getEventName();
        // Assert
        assertEquals("empresa.deleted", result);
    }

    // ── occurredOn

    @Test
    @DisplayName("occurredOn() no debe ser nulo y debe quedar acotado al instante de construcción")
    void shouldRecordOccurredOnAtCreationTime() {
        // Arrange
        final LocalDateTime before = LocalDateTime.now();
        final EmpresaDeleteDomainEvent event = new EmpresaDeleteDomainEvent(new EmpresaId(EMPRESA_ID));
        final LocalDateTime after = LocalDateTime.now();
        // Act
        final LocalDateTime occurredOn = event.getOccurredOn();
        // Assert
        assertNotNull(occurredOn, "occurredOn no debe ser null");
        assertFalse(
                occurredOn.isBefore(before),
                "occurredOn debe ser >= al instante anterior a la construcción");
        assertFalse(
                occurredOn.isAfter(after),
                "occurredOn debe ser <= al instante posterior a la construcción");
    }

    // ── userId()

    @Test
    @DisplayName("idEmpresa() debe devolver la misma instancia de EmpresaId recibida en el constructor")
    void shouldReturnSameUserIdInstance() {
        // Arrange
        final EmpresaId idEmpresa = new EmpresaId(EMPRESA_ID);
        final EmpresaDeleteDomainEvent event = new EmpresaDeleteDomainEvent(idEmpresa);
        // Act
        final EmpresaId result = event.getIdEmpresa();
        // Assert
        assertSame(idEmpresa, result);
    }

    // ── payload()

    @Test
    @DisplayName("payload() debe contener únicamente la entrada 'id' con el valor del UserId")
    void shouldReturnPayloadWithOnlyUserId() {
        // Arrange
        final EmpresaDeleteDomainEvent event = new EmpresaDeleteDomainEvent(new EmpresaId(EMPRESA_ID));

        // Act
        final Map<String, String> payload = event.payload();

        // Assert
        assertAll(
                "payload de EmpresaDeleteDomainEvent",
                () -> assertEquals(1, payload.size(), "el mapa debe tener exactamente 1 entrada"),
                () -> assertEquals(EMPRESA_ID, payload.get("id"), "id"));
    }
}
