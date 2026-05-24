package com.jcaa.usersmanagement.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaIncorporationDate;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaName;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("EmpresaModel")
class EmpresaModelTest {

    // ── Arrange (variables globales para la mayoría de tests)

    private EmpresaId                   idEmpresa;
    private EmpresaName                 nameEmpresa;
    private EmpresaIncorporationDate    incorporationDate;
    private EmpresaAnnualBilling        annualBilling;
    private EmpresaSede                 sede;
    private EmpresaSector               sector;

    @BeforeEach
    void setUp() {
        idEmpresa = new EmpresaId(UUID.randomUUID().toString());
        nameEmpresa = new EmpresaName("Fortuna");
        incorporationDate = new EmpresaIncorporationDate(LocalDate.of(2025, 4, 8));
        annualBilling = new EmpresaAnnualBilling(new BigDecimal("10000000"));
        sede = new EmpresaSede("Fouver", "Enfocada en software.");
        sector = new EmpresaSector("Tecnologia", "Desarrollo de software.");
    }

    // ── createEmpresa()

    @Test
    @DisplayName("create() debe preservar todos los campos recibidos")
    void shouldCreateEmpresaAndPreserveAllFields() {
        // Act
        final EmpresaModel empresa = EmpresaModel.create(idEmpresa, nameEmpresa, incorporationDate, annualBilling, sede, sector);
        // Assert
        assertAll("create() factory",
                () -> assertSame(idEmpresa, empresa.getIdEmpresa(), "idEmpresa debe preservarse"),
                () -> assertSame(nameEmpresa, empresa.getNameEmpresa(), "nameEmpresa debe preservarse"),
                () -> assertSame(incorporationDate, empresa.getIncorporationDate(), "incorporationDate debe preservarse"),
                () -> assertSame(annualBilling, empresa.getAnnualBilling(), "annualBilling debe preservarse"),
                () -> assertSame(sede, empresa.getSede(), "sede debe preservarse"),
                () -> assertSame(sector, empresa.getSector(), "sector debe preservarse")
        );
    }

    // ── updateAnnualBilling()

    @Test
    @DisplayName("updateAnnualBilling() retorna nueva instancia y preserva los demás campos")
    void shouldUpdateAnnualBillingAndPreserveOtherFields() {
        // Arrange
        final EmpresaModel empresa = EmpresaModel.create(idEmpresa, nameEmpresa, incorporationDate, annualBilling, sede, sector);
        final EmpresaAnnualBilling newAnnualBilling = new EmpresaAnnualBilling(new BigDecimal("20000000"));
        // Act
        final EmpresaModel updated = empresa.updateAnnualBilling(newAnnualBilling);
        // Assert
        assertAll("resultado de updateAnnualBilling()",
                () -> assertNotSame(empresa, updated),
                () -> assertSame(idEmpresa, updated.getIdEmpresa()),
                () -> assertSame(nameEmpresa, updated.getNameEmpresa()),
                () -> assertSame(incorporationDate, updated.getIncorporationDate()),
                () -> assertEquals(newAnnualBilling, updated.getAnnualBilling()),
                () -> assertSame(sede, updated.getSede()),
                () -> assertSame(sector, updated.getSector())
        );
    }

    // ── changeSede()

    @Test
    @DisplayName("changeSede() retorna nueva instancia y preserva los demás campos")
    void shouldChangeSedeAndPreserveOtherFields() {
        // Arrange
        final EmpresaModel empresa = EmpresaModel.create(idEmpresa, nameEmpresa, incorporationDate, annualBilling, sede, sector);
        final EmpresaSede newSede = new EmpresaSede("Bogotá", "Centro de innovación");
        // Act
        final EmpresaModel updated = empresa.changeSede(newSede);
        // Assert
        assertAll("resultado de changeSede()",
                () -> assertNotSame(empresa, updated),
                () -> assertSame(idEmpresa, updated.getIdEmpresa()),
                () -> assertSame(nameEmpresa, updated.getNameEmpresa()),
                () -> assertSame(incorporationDate, updated.getIncorporationDate()),
                () -> assertSame(annualBilling, updated.getAnnualBilling()),
                () -> assertEquals(newSede, updated.getSede()),
                () -> assertSame(sector, updated.getSector())
        );
    }

    // ── changeSector()

    @Test
    @DisplayName("changeSector() retorna nueva instancia y preserva los demás campos")
    void shouldChangeSectorAndPreserveOtherFields() {
        // Arrange
        final EmpresaModel empresa = EmpresaModel.create(idEmpresa, nameEmpresa, incorporationDate, annualBilling, sede, sector);
        final EmpresaSector newSector = new EmpresaSector("Manufactura", "Producción de piezas industriales");
        // Act
        final EmpresaModel updated = empresa.changeSector(newSector);
        // Assert
        assertAll("resultado de changeSector()",
                () -> assertNotSame(empresa, updated, "debe ser una nueva instancia"),
                () -> assertSame(idEmpresa, updated.getIdEmpresa()),
                () -> assertSame(nameEmpresa, updated.getNameEmpresa()),
                () -> assertSame(incorporationDate, updated.getIncorporationDate()),
                () -> assertSame(annualBilling, updated.getAnnualBilling()),
                () -> assertSame(sede, updated.getSede()),
                () -> assertEquals(newSector, updated.getSector()
                )
        );
    }
}