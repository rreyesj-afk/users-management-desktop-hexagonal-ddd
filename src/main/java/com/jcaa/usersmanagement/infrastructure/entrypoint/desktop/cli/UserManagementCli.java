package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli;

import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler.*;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu.UserMenuOption;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public final class UserManagementCli {

  private static final String MENU_BORDER = "  ==========================================";

  private final UserController userController;
  private final ConsoleIO console;

  public void start() {
    final UserResponsePrinter printer = new UserResponsePrinter(console);
    runLoop(buildHandlers(printer));
  }

  private void runLoop(final Map<UserMenuOption, OperationHandler> handlers) {
    boolean running = true;
    while (running) {
      printMenu();
      final int choice = console.readInt("\n  Option: ");
      final Optional<UserMenuOption> option = UserMenuOption.fromNumber(choice);

      if (option.isEmpty()) {
        console.println("  Invalid option. Please try again.");
      } else if (option.get() == UserMenuOption.EXIT) {
        console.println("\n  Goodbye!\n");
        running = false;
      } else {
        executeHandler(handlers, option.get());
      }
    }
  }

  private void executeHandler(
          final Map<UserMenuOption, OperationHandler> handlers, final UserMenuOption option) {
    try {
      handlers.get(option).handle();
    } catch (final ConstraintViolationException exception) {
      console.println("  Validation errors:");
      exception.getConstraintViolations()
              .forEach(violation -> console.println("    - " + violation.getMessage()));
    } catch (final RuntimeException exception) {
      console.println("  Unexpected error: " + exception.getMessage());
    }
  }

  private Map<UserMenuOption, OperationHandler> buildHandlers(final UserResponsePrinter printer) {
    return Map.of(
            UserMenuOption.LIST_USERS,  new ListUsersHandler(userController, printer),
            UserMenuOption.FIND_USER,   new FindUserByIdHandler(userController, console, printer),
            UserMenuOption.CREATE_USER, new CreateUserHandler(userController, console, printer),
            UserMenuOption.UPDATE_USER, new UpdateUserHandler(userController, console, printer),
            UserMenuOption.DELETE_USER, new DeleteUserHandler(userController, console),
            UserMenuOption.LOGIN,       new LoginHandler(userController, console, printer));
  }

  private void printMenu() {
    console.println();
    console.println(MENU_BORDER);
    console.println("    Main Menu");
    console.println(MENU_BORDER);
    for (final UserMenuOption option : UserMenuOption.values()) {
      console.printf("    [%d] %s%n", option.getNumber(), option.getDescription());
    }
    console.println(MENU_BORDER);
  }
}