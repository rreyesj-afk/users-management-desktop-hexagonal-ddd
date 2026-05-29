package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.*;
import com.jcaa.usersmanagement.application.service.dto.command.*;
import com.jcaa.usersmanagement.application.service.dto.query.GetEmpresaByIdQuery;
import com.jcaa.usersmanagement.domain.exception.EmpresaAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.EmpresaNotFoundException;
import com.jcaa.usersmanagement.domain.model.EmpresaModel;
import com.jcaa.usersmanagement.domain.valueobject.*;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("EmpresaController")
@ExtendWith(MockitoExtension.class)
class EmpresaControllerTest {

    @Mock
    private CreateEmpresaUseCase createEmpresaUseCase;
    @Mock
    private UpdateEmpresaAnnualBillingUseCase updateEmpresaAnnualBillingUseCase;
    @Mock
    private ChangeEmpresaSedeUseCase  changeEmpresaSedeUseCase;
    @Mock
    private ChangeEmpresaSectorUseCase  changeEmpresaSectorUseCase;
    @Mock
    private GetEmpresaByIdUseCase  getEmpresaByIdUseCase;
    @Mock
    private GetAllEmpresasUseCase  getAllEmpresasUseCase;
    @Mock
    private DeleteEmpresaUseCase deleteEmpresaUseCase;

    private EmpresaController empresaController;

    //helpers

    private static EmpresaModel buildEmpresa(
            final String idEmpresa,
            final String nameEmpresa,
            final LocalDate incorporationDate,
            final BigDecimal annualBilling,
            final String sedeName,
            final String sedeDescription,
            final String sectorName,
            final String sectorDescription
            ) {
        return new EmpresaModel(
                new EmpresaId(idEmpresa),
                new EmpresaName(nameEmpresa),
                new EmpresaIncorporationDate(incorporationDate),
                new EmpresaAnnualBilling(annualBilling),
                new EmpresaSede(sedeName, sedeDescription),
                new EmpresaSector(sectorName, sectorDescription)
        );
    }

    @BeforeEach
    void setUp() {
        empresaController = new EmpresaController(
                createEmpresaUseCase,
                updateEmpresaAnnualBillingUseCase,
                changeEmpresaSedeUseCase,
                changeEmpresaSectorUseCase,
                getEmpresaByIdUseCase,
                getAllEmpresasUseCase,
                deleteEmpresaUseCase
        );
    }

    // listAllEmpresas

    @Test
    @DisplayName("listAllUsers() devuelve una lista EmpresaResponse correctamente mapeada cuando el caso de uso devuelve empresas")
    void listAllEmpresas_returnsMappedResponseList_whenEmpresasExist() {
        // Arrange
        final EmpresaModel empresa = buildEmpresa(
                "1",
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );
        when(getAllEmpresasUseCase.execute()).thenReturn(List.of(empresa));
        // Act
        final List<EmpresaResponse> result = empresaController.listAllEmpresas();
        // Assert
        assertAll("single-empresas list mapping",
                () -> assertEquals(1, result.size(), "list must contain exactly one element"),
                () -> assertEquals("EMP-0001", result.get(0).idEmpresa(), "id must match"),
                () -> assertEquals("Fortuna", result.get(0).nameEmpresa(), "name must match"),
                () -> assertEquals(LocalDate.of(2025, 4, 8).toString(), result.get(0).incorporationDate(), "incorporation date must match"),
                () -> assertEquals(new BigDecimal("10000000").toString(), result.get(0).annualBilling(), "annual billing must match"),
                () -> assertEquals("Fouver", result.get(0).sedeName(), "sede name must match"),
                () -> assertEquals("Enfocada en software.", result.get(0).sedeDescription(), "sede description must match"),
                () -> assertEquals("Tecnología", result.get(0).sectorName(), "sector name must match"),
                () -> assertEquals("Desarrollo de software.", result.get(0).sectorDescription(), "sector description must match")
        );

        verify(getAllEmpresasUseCase).execute();
    }

    @Test
    @DisplayName("listAllEmpresas() devuelve una lista vacía cuando el caso de uso no devuelve ninguna empresa")
    void listAllEmpresas_returnsEmptyList_whenNoEmpresasExist() {
        // Arrange
        when(getAllEmpresasUseCase.execute()).thenReturn(List.of());
        // Act
        final List<EmpresaResponse> result = empresaController.listAllEmpresas();
        // Assert
        assertTrue(result.isEmpty(), "result must be an empty list");
        verify(getAllEmpresasUseCase).execute();
    }

    // findEmpresaById

    @Test
    @DisplayName("findEmpresaById() construye una consulta GetEmpresaByIdQuery con el id proporcionado y devuelve la respuesta mapeada")
    void findFindEmpresaById_returnsMappedResponse_whenEmpresaExists() {
        // Arrange
        final EmpresaModel empresa = buildEmpresa(
                "2",
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        final ArgumentCaptor<GetEmpresaByIdQuery> captor = ArgumentCaptor.forClass(GetEmpresaByIdQuery.class);
        when(getEmpresaByIdUseCase.execute(captor.capture())).thenReturn(empresa);
        // Act
        final EmpresaResponse result = empresaController.findEmpresaById("EMP-0002");
        // Assert
        assertAll("findEmpresaById response mapping",
                () -> assertEquals("EMP-0002", result.idEmpresa(), "id must match"),
                () -> assertEquals("Fortuna", result.nameEmpresa(), "name must match"),
                () -> assertEquals(LocalDate.of(2025, 4, 8).toString(), result.incorporationDate(), "incorporation date must match"),
                () -> assertEquals(new BigDecimal("10000000").toString(), result.annualBilling(), "annual billing must match"),
                () -> assertEquals("Fouver", result.sedeName(), "sede name must match"),
                () -> assertEquals("Enfocada en software.", result.sedeDescription(), "sede description must match"),
                () -> assertEquals("Tecnología", result.sectorName(), "sector name must match"),
                () -> assertEquals("Desarrollo de software.", result.sectorDescription(), "sector description must match"),
                () -> assertEquals("EMP-0002", captor.getValue().idEmpresa(), "query id must match provided id")
        );
    }

    @Test
    @DisplayName("findEmpresaById() propaga EmpresaNotFoundException cuando el caso de uso no puede encontrar la empresa")
    void findEmpresaById_propagatesEmpresaNotFoundException_whenEmpresaDoesNotExist() {
        // Arrange
        when(getEmpresaByIdUseCase.execute(new GetEmpresaByIdQuery("EMP-0099"))).thenThrow(EmpresaNotFoundException.becauseIdWasNotFound("EMP-0001"));
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> empresaController.findEmpresaById("EMP-0099"), "EmpresaNotFoundException must propagate without being wrapped");
    }

    // ── createEmpresa

    @Test
    @DisplayName("createEmpresa() delega un CreateEmpresaCommand correctamente poblado y devuelve la respuesta mapeada")
    void createEmpresa_delegatesCorrectCommandAndReturnsMappedResponse_whenCreationSucceeds() {
        // Arrange
        final CreateEmpresaRequest request = new CreateEmpresaRequest(
                "Fortuna",
                LocalDate.of(2025, 4, 8).toString(),
                new BigDecimal("10000000").toString(),
                "Foruver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        final EmpresaModel createdEmpresa = buildEmpresa(
                "3", // <- importante: ID del dominio SIN formato
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Foruver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        final ArgumentCaptor<CreateEmpresaCommand> captor = ArgumentCaptor.forClass(CreateEmpresaCommand.class);
        when(createEmpresaUseCase.execute(captor.capture())).thenReturn(createdEmpresa);
        // Act
        final EmpresaResponse result = empresaController.createEmpresa(request);
        // Assert
        assertAll("createEmpresa command delegation and response mapping",
                () -> assertEquals("Fortuna", captor.getValue().nameEmpresa(), "command name must match request name"),
                () -> assertEquals(LocalDate.of(2025, 4, 8), captor.getValue().incorporationDate(), "command incorporation date must match request incorporationDate"),
                () -> assertEquals(new BigDecimal("10000000"), captor.getValue().annualBilling(), "command annual billing must match request annual billing"),
                () -> assertEquals("Foruver", captor.getValue().sedeName(), "command sede name must match request sede name"),
                () -> assertEquals("Enfocada en software.", captor.getValue().sedeDescription(), "command sede description must match request sede description"),
                () -> assertEquals("Tecnología", captor.getValue().sectorName(), "command sector name must match request sector name"),
                () -> assertEquals("Desarrollo de software.", captor.getValue().sectorDescription(), "command sector description must match request sector description"),
                () -> assertEquals("EMP-0003", result.idEmpresa(), "response id must come formatted from mapper")
        );

        verify(createEmpresaUseCase).execute(any(CreateEmpresaCommand.class));
    }

    @Test
    @DisplayName("createEmpresa() propaga EmpresaAlreadyExistsException cuando el caso de uso rechaza un nombre duplicado.")
    void createEmpresa_propagatesEmpresaAlreadyExistsException_whenNameIsDuplicated() {
        // Arrange
        final CreateEmpresaRequest request = new CreateEmpresaRequest(
                "Fortuna",
                LocalDate.of(2025,4,8).toString(),
                new BigDecimal("10000000").toString(),
                "Fouver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        when(createEmpresaUseCase.execute(any())).thenThrow(EmpresaAlreadyExistsException.becauseNameAlreadyExists("Fortuna"));
        // Act & Assert
        assertThrows(EmpresaAlreadyExistsException.class, () -> empresaController.createEmpresa(request), "EmpresaAlreadyExistsException must propagate without being wrapped");
    }

    // updateAnnualBilling

    @Test
    @DisplayName("updateAnnualBilling() delega un UpdateEmpresaAnnualBillingCommand correctamente poblado y devuelve la respuesta mapeada")
    void updateAnnualBilling_delegatesCorrectCommandAndReturnsMappedResponse_whenUpdateSucceeds() {
        // Arrange
        final UpdateEmpresaAnnualBillingRequest request = new UpdateEmpresaAnnualBillingRequest("EMP-0005", new BigDecimal("20000000").toString());
        final EmpresaModel updatedEmpresa = buildEmpresa(
                "5",
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("20000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        final ArgumentCaptor<UpdateEmpresaAnnualBillingCommand> captor = ArgumentCaptor.forClass(UpdateEmpresaAnnualBillingCommand.class);
        when(updateEmpresaAnnualBillingUseCase.execute(captor.capture())).thenReturn(updatedEmpresa);
        // Act
        final EmpresaResponse result = empresaController.updateAnnualBilling(request);
        // Assert
        assertAll("updateAnnualBilling command delegation and response mapping",
                () -> assertEquals("EMP-0005", captor.getValue().idEmpresa(), "command id empresa must match request id empresa"),
                () -> assertEquals(new BigDecimal("20000000"), captor.getValue().annualBilling(), "command annual billing must match request annual billing"),
                () -> assertEquals("EMP-0005", result.idEmpresa(), "response id must match")
        );

        verify(updateEmpresaAnnualBillingUseCase)
                .execute(any(UpdateEmpresaAnnualBillingCommand.class));
    }

    @Test
    @DisplayName("updateAnnualBilling() propaga EmpresaNotFoundException cuando el caso de uso no puede encontrar la empresa")
    void updateAnnualBilling_propagatesEmpresaNotFoundException_whenEmpresaDoesNotExist() {
        // Arrange
        final UpdateEmpresaAnnualBillingRequest request = new UpdateEmpresaAnnualBillingRequest(
                "EMP-0999",
                new BigDecimal("10000000").toString()
        );
        when(updateEmpresaAnnualBillingUseCase.execute(any())).thenThrow(EmpresaNotFoundException.becauseIdWasNotFound("EMP-0999"));
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> empresaController.updateAnnualBilling(request), "EmpresaNotFoundException must propagate without being wrapped");
    }

    // changeEmpresaSede

    @Test
    @DisplayName("changeEmpresaSede() delegates a correctly populated ChangeEmpresaSedeCommand and returns the mapped response")
    void updateChangeEmpresaSede_delegatesCorrectCommandAndReturnsMappedResponse_whenChangeSucceeds() {
        // Arrange
        final ChangeEmpresaSedeRequest request = new ChangeEmpresaSedeRequest("EMP-0006", "Clouur", "Enfocada en hardware.");

        final EmpresaModel changeEmpresaSede = buildEmpresa(
                "6",
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        final ArgumentCaptor<ChangeEmpresaSedeCommand> captor = ArgumentCaptor.forClass(ChangeEmpresaSedeCommand.class);
        when(changeEmpresaSedeUseCase.execute(captor.capture())).thenReturn(changeEmpresaSede);
        // Act
        final EmpresaResponse result = empresaController.changeSede(request);
        // Assert
        assertAll("changeEmpresaSede command delegation and response mapping",
                () -> assertEquals("EMP-0006", captor.getValue().idEmpresa(), "command id empresa must match request id empresa"),
                () -> assertEquals("Clouur", captor.getValue().sedeName(), "command sede name must match request sede name"),
                () -> assertEquals("Enfocada en hardware.", captor.getValue().sedeDescription(), "command sede description must match request sede description"),
                () -> assertEquals("EMP-0006", result.idEmpresa(), "response id must match")
        );
    }

    @Test
    @DisplayName("changeEmpresaSede() propaga EmpresaNotFoundException cuando el caso de uso no puede encontrar la empresa")
    void changeEmpresaSede_propagatesUserNotFoundException_whenEmpresaDoesNotExist() {
        // Arrange
        final UpdateEmpresaAnnualBillingRequest request = new UpdateEmpresaAnnualBillingRequest(
                "EMP-0999",
                new BigDecimal("10000000").toString()
        );
        when(updateEmpresaAnnualBillingUseCase.execute(any())).thenThrow(EmpresaNotFoundException.becauseIdWasNotFound("EMP-0999"));
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> empresaController.updateAnnualBilling(request), "EmpresaNotFoundException must propagate without being wrapped");
    }

    // changeEmpresaSector

    @Test
    @DisplayName("changeEmpresaSector() delegates a correctly populated ChangeEmpresaSectorCommand and returns the mapped response")
    void updateChangeEmpresaSector_delegatesCorrectCommandAndReturnsMappedResponse_whenChangeSucceeds() {
        // Arrange
        final ChangeEmpresaSectorRequest request = new ChangeEmpresaSectorRequest("EMP-0007", "Hardware", "Creación y mantenimiento de hardware.");

        final EmpresaModel changeEmpresaSector = buildEmpresa(
                "7",
                "Fortuna",
                LocalDate.of(2025, 4, 8),
                new BigDecimal("10000000"),
                "Fouver",
                "Enfocada en software.",
                "Tecnología",
                "Desarrollo de software."
        );

        final ArgumentCaptor<ChangeEmpresaSectorCommand> captor = ArgumentCaptor.forClass(ChangeEmpresaSectorCommand.class);
        when(changeEmpresaSectorUseCase.execute(captor.capture())).thenReturn(changeEmpresaSector);
        // Act
        final EmpresaResponse result = empresaController.changeSector(request);
        // Assert
        assertAll("changeEmpresaSector command delegation and response mapping",
                () -> assertEquals("EMP-0007", captor.getValue().idEmpresa(), "command id empresa must match request id empresa"),
                () -> assertEquals("Hardware", captor.getValue().sectorName(), "command sector name must match request sector name"),
                () -> assertEquals("Creación y mantenimiento de hardware.", captor.getValue().sectorDescription(), "command sector description must match request sector description"),
                () -> assertEquals("EMP-0007", result.idEmpresa(), "response id must match")
        );
    }

    @Test
    @DisplayName("changeEmpresaSector() propaga EmpresaNotFoundException cuando el caso de uso no puede encontrar la empresa")
    void changeEmpresaSector_propagatesEmpresaNotFoundException_whenEmpresaDoesNotExist() {
        // Arrange
        final ChangeEmpresaSectorRequest request = new ChangeEmpresaSectorRequest(
                "EMP-0999",
                "Hardware",
                "Enfocada em hardware."
        );
        when(changeEmpresaSectorUseCase.execute(any())).thenThrow(EmpresaNotFoundException.becauseIdWasNotFound("EMP-0999"));
        // Act & Assert
        assertThrows(EmpresaNotFoundException.class, () -> empresaController.changeSector(request), "EmpresaNotFoundException must propagate without being wrapped");
    }

    // deleteEmpresa

    @Test
    @DisplayName("deleteEmpresa() delegates a DeleteUserCommand with the given id to the use case")
    void deleteEmpresa_delegatesDeleteCommandWithCorrectId() {
        // Arrange
        final ArgumentCaptor<DeleteEmpresaCommand> captor =
                ArgumentCaptor.forClass(DeleteEmpresaCommand.class);
        doNothing().when(deleteEmpresaUseCase).execute(captor.capture());
        // Act
        empresaController.deleteEmpresa("EMP-0008");
        // Assert
        assertEquals("EMP-0008", captor.getValue().idEmpresa(), "delete command id must match the provided id");
    }

    @Test
    @DisplayName("deleteEmpresa() propagates EmpresaNotFoundException when the use case cannot find the user")
    void deleteEmpresa_propagatesEmpresaNotFoundException_whenEmpresaDoesNotExist() {
        // Arrange
        doThrow(EmpresaNotFoundException.becauseIdWasNotFound("EMP-0999")).when(deleteEmpresaUseCase).execute(any());
        // Act & Assert
        assertThrows(
                EmpresaNotFoundException.class,
                () -> empresaController.deleteEmpresa("EMP-0999"),
                "EmpresaNotFoundException must propagate without being wrapped");
    }
}