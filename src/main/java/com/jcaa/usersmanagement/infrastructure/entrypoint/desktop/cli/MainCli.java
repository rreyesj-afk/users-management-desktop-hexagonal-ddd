package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.MainMenuOption;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public final class MainCli {

    private static final String BANNER =
            """
            ==========================================
                 Management System
            ==========================================
            """;

    private static final String MENU_BORDER =
            "  ==========================================";

    private final UserManagementCli userCli;
    private final HoldingEmpresaCli empresaCli;
    private final ConsoleIO console;

    public void start() {
        console.println(BANNER);
        boolean running = true;
        while (running) {
            printMenu();
            final int choice = console.readInt("\n  Option: ");
            final Optional<MainMenuOption> option = MainMenuOption.fromNumber(choice);
            if (option.isEmpty()) {
                console.println("  Invalid option. Please try again.");
                continue;
            }
            switch (option.get()) {
                case USER_MANAGEMENT -> userCli.start();
                case EMPRESA_MANAGEMENT -> empresaCli.start();
                case EXIT -> {
                    console.println("\n  Goodbye!\n");
                    running = false;
                }
            }
        }
    }

    private void printMenu() {
        console.println();
        console.println(MENU_BORDER);
        console.println("    Main Menu");
        console.println(MENU_BORDER);
        for (final MainMenuOption option : MainMenuOption.values()) {
            console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
        }

        console.println(MENU_BORDER);
    }
}