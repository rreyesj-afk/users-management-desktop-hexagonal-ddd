package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSectorCommand;
import com.jcaa.usersmanagement.application.service.dto.command.ChangeEmpresaSedeCommand;
import com.jcaa.usersmanagement.application.service.dto.command.CreateEmpresaCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteEmpresaCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateEmpresaAnnualBillingCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaAnnualBilling;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaId;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSector;
import com.jcaa.usersmanagement.domain.valueobject.EmpresaSede;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EmpresaApplicationMapper")
class EmpresaApplicationMapperTest {

    private static final String     EMPRESA_ID              = "0001";
    private static final String     EMPRESA_NAME            = "Fortuna";
    private static final LocalDate  INCORPORATION_DATE      = LocalDate.of(2025, 4, 8);
    private static final BigDecimal ANNUAL_BILLING          = new BigDecimal("10000000");
    private static final String     SEDE_NAME               = "Fouver";
    private static final String     SEDE_DESCRIPTION        = "Enfocada en software.";
    private static final String     SECTOR_NAME             = "Tecnologia";
    private static final String     SECTOR_DESCRIPTION      = "Desarrollo de software.";

    @Test
    @DisplayName("fromCreateCommandToModel() debe mapear correctamente a EmpresaModel")
    void shouldMapCreateCommandToEmpresaModel() {
        // Arrange
        final CreateEmpresaCommand command = new CreateEmpresaCommand(
                EMPRESA_NAME,
                INCORPORATION_DATE,
                ANNUAL_BILLING,
                SEDE_NAME,
                SEDE_DESCRIPTION,
                SECTOR_NAME,
                SECTOR_DESCRIPTION
        );
        // Act
        final EmpresaModel result = EmpresaApplicationMapper.fromCreateCommandToModel(command);
        // Assert
        assertAll("mapped empresa model",
                () -> assertEquals("0",        result.getIdEmpresa().value(),              "idEmpresa"),
                () -> assertEquals(EMPRESA_NAME,        result.getNameEmpresa().value(),            "nameEmpresa"),
                () -> assertEquals(INCORPORATION_DATE,  result.getIncorporationDate().value(),      "incorporationDate"),
                () -> assertEquals(ANNUAL_BILLING,      result.getAnnualBilling().value(),          "annualBilling"),
                () -> assertEquals(SEDE_NAME,           result.getSede().sedeName(),                "sedeName"),
                () -> assertEquals(SEDE_DESCRIPTION,    result.getSede().sedeDescription(),         "sedeDescription"),
                () -> assertEquals(SECTOR_NAME,         result.getSector().sectorName(),            "sectorName"),
                () -> assertEquals(SECTOR_DESCRIPTION,  result.getSector().sectorDescription(),     "sectorDescription")
        );
    }

    @Test
    @DisplayName("fromUpdateAnnualBillingCommandToEmpresaId() debe mapear correctamente")
    void shouldMapUpdateAnnualBillingCommandToEmpresaId() {
        // Arrange
        final UpdateEmpresaAnnualBillingCommand command = new UpdateEmpresaAnnualBillingCommand(EMPRESA_ID, ANNUAL_BILLING);
        // Act
        final EmpresaId result = EmpresaApplicationMapper.fromUpdateAnnualBillingCommandToEmpresaId(command);
        // Assert
        assertEquals(EMPRESA_ID, result.value());
    }

    @Test
    @DisplayName("fromUpdateAnnualBillingCommandToAnnualBilling() debe mapear correctamente")
    void shouldMapUpdateAnnualBillingCommandToAnnualBilling() {
        // Arrange
        final UpdateEmpresaAnnualBillingCommand command = new UpdateEmpresaAnnualBillingCommand(EMPRESA_ID, ANNUAL_BILLING);
        // Act
        final EmpresaAnnualBilling result = EmpresaApplicationMapper.fromUpdateAnnualBillingCommandToAnnualBilling(command);
        // Assert
        assertEquals(ANNUAL_BILLING, result.value());
    }

    @Test
    @DisplayName("fromChangeSedeCommandToEmpresaId() debe mapear correctamente")
    void shouldMapChangeSedeCommandToEmpresaId() {
        // Arrange
        final ChangeEmpresaSedeCommand command = new ChangeEmpresaSedeCommand(EMPRESA_ID, SEDE_NAME, SEDE_DESCRIPTION);
        // Act
        final EmpresaId result = EmpresaApplicationMapper.fromChangeSedeCommandToEmpresaId(command);
        // Assert
        assertEquals(EMPRESA_ID, result.value());
    }

    @Test
    @DisplayName("fromChangeSedeCommandToSede() debe mapear correctamente")
    void shouldMapChangeSedeCommandToSede() {
        // Arrange
        final ChangeEmpresaSedeCommand command = new ChangeEmpresaSedeCommand(EMPRESA_ID, SEDE_NAME, SEDE_DESCRIPTION);
        // Act
        final EmpresaSede result = EmpresaApplicationMapper.fromChangeSedeCommandToSede(command);
        // Assert
        assertAll(
                () -> assertEquals(SEDE_NAME,           result.sedeName()),
                () -> assertEquals(SEDE_DESCRIPTION,    result.sedeDescription())
        );
    }

    @Test
    @DisplayName("fromChangeSectorCommandToEmpresaId() debe mapear correctamente")
    void shouldMapChangeSectorCommandToEmpresaId() {
        // Arrange
        final ChangeEmpresaSectorCommand command = new ChangeEmpresaSectorCommand(EMPRESA_ID, SECTOR_NAME, SECTOR_DESCRIPTION);
        // Act
        final EmpresaId result = EmpresaApplicationMapper.fromChangeSectorCommandToEmpresaId(command);
        // Assert
        assertEquals(EMPRESA_ID, result.value());
    }

    @Test
    @DisplayName("fromChangeSectorCommandToSector() debe mapear correctamente")
    void shouldMapChangeSectorCommandToSector() {
        // Arrange
        final ChangeEmpresaSectorCommand command = new ChangeEmpresaSectorCommand(EMPRESA_ID, SECTOR_NAME, SECTOR_DESCRIPTION);
        // Act
        final EmpresaSector result = EmpresaApplicationMapper.fromChangeSectorCommandToSector(command);
        // Assert
        assertAll(
                () -> assertEquals(SECTOR_NAME,         result.sectorName()),
                () -> assertEquals(SECTOR_DESCRIPTION,  result.sectorDescription())
        );
    }

    @Test
    @DisplayName("fromGetEmpresaByIdQueryToEmpresaId() debe mapear correctamente")
    void shouldMapGetEmpresaByIdQueryToEmpresaId() {
        // Arrange
        final GetEmpresaByIdQuery query = new GetEmpresaByIdQuery(EMPRESA_ID);
        // Act
        final EmpresaId result = EmpresaApplicationMapper.fromGetEmpresaByIdQueryToEmpresaId(query);
        // Assert
        assertEquals(EMPRESA_ID, result.value());
    }

    @Test
    @DisplayName("fromDeleteCommandToEmpresaId() debe mapear correctamente")
    void shouldMapDeleteCommandToEmpresaId() {
        // Arrange
        final DeleteEmpresaCommand command = new DeleteEmpresaCommand(EMPRESA_ID);
        // Act
        final EmpresaId result = EmpresaApplicationMapper.fromDeleteCommandToEmpresaId(command);
        // Assert
        assertEquals(EMPRESA_ID, result.value()
        );
    }
}
