package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu;

import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmpresaMenuOption {

    LIST_EMPRESAS(          1, "List all empresas"),
    FIND_EMPRESA(           2, "Find empresa by ID"),
    CREATE_EMPRESA(         3, "Create empresa"),
    UPDATE_ANNUAL_BILLING(  4, "Update annual billing"),
    CHANGE_SEDE(            5, "Change sede"),
    CHANGE_SECTOR(          6, "Change sector"),
    DELETE_EMPRESA(         7, "Delete empresa"),
    EXIT(                   0, "Exit");

    private final int number;
    private final String description;

    public static Optional<EmpresaMenuOption>fromNumber(final int number) {

        for (final EmpresaMenuOption option : values()) {
            if (option.number == number) {
                return Optional.of(option);
            }
        }
        return Optional.empty();
    }
}