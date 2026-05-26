package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EmpresaNotificationsService")
class EmpresaNotificationsServiceTest {

    private EmpresaNotificationsService service;
    private EmpresaModel empresa;
    private TestLogHandler logHandler;

    @BeforeEach
    void setUp() {
        service = new EmpresaNotificationsService();
        empresa = EmpresaModel.create(
                new EmpresaId("1"),
                new EmpresaName("Fortuna"),
                new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8)),
                new EmpresaAnnualBilling(new BigDecimal("10000000")),
                new EmpresaSede("Fouver", "Enfocada en software."),
                new EmpresaSector("Tecnologia", "Desarrollo de software")
        );

        final Logger logger = Logger.getLogger(EmpresaNotificationsService.class.getName());

        logHandler = new TestLogHandler();

        logger.addHandler(logHandler);

        logger.setUseParentHandlers(false);
    }

    @Test
    @DisplayName("notifyEmpresaCreated() debe escribir log de empresa creada")
    void shouldLogEmpresaCreatedMessage() {
        // Act
        service.notifyEmpresaCreated(empresa);
        // Assert
        assertTrue(logHandler.getLogs().stream().anyMatch(log -> log.contains("[EMPRESA CREATED]") && log.contains("Fortuna")));
    }

    @Test
    @DisplayName("notifyAnnualBillingUpdated() debe escribir log de facturación")
    void shouldLogAnnualBillingUpdatedMessage() {
        // Act
        service.notifyAnnualBillingUpdated(empresa);
        // Assert
        assertTrue(logHandler.getLogs().stream().anyMatch(log -> log.contains("10000000")));
    }

    @Test
    @DisplayName("notifySedeChanged() debe escribir log de sede")
    void shouldLogSedeChangedMessage() {
        // Act
        service.notifySedeChanged(empresa);
        // Assert
        assertTrue(logHandler.getLogs().stream().anyMatch(log -> log.contains("Fouver")));
    }

    @Test
    @DisplayName("notifySectorChanged() debe escribir log de sector")
    void shouldLogSectorChangedMessage() {
        // Act
        service.notifySectorChanged(empresa);
        // Assert
        assertTrue(logHandler.getLogs().stream().anyMatch(log -> log.contains("Tecnologia")));
    }

    @Test
    @DisplayName("notifyEmpresaDeleted() debe escribir log de eliminación")
    void shouldLogEmpresaDeletedMessage() {
        // Act
        service.notifyEmpresaDeleted(empresa);
        // Assert
        assertTrue(logHandler.getLogs().stream().anyMatch(log -> log.contains("[EMPRESA DELETED]")));
    }

    @Getter
    private static class TestLogHandler extends Handler {
        private final List<String> logs = new ArrayList<>();

        @Override
        public void publish(final LogRecord record) {
            logs.add(record.getMessage());
        }

        @Override
        public void flush() {}

        @Override
        public void close() {}

    }
}