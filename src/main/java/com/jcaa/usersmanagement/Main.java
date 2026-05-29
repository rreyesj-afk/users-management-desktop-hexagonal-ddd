package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.infrastructure.config.DependencyContainer;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.HoldingEmpresaCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.MainCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.UserManagementCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public final class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(final String[] args) {
    log.info("Starting Management System...");
    final DependencyContainer container = new DependencyContainer();
    try (final Scanner scanner = new Scanner(System.in)) {
      final ConsoleIO console = new ConsoleIO(scanner, System.out);

      final UserManagementCli userCli = new UserManagementCli(container.userController(), console);

      final HoldingEmpresaCli empresaCli = new HoldingEmpresaCli(container.empresaController(), console);

      final MainCli mainCli = new MainCli(userCli, empresaCli, console);

      mainCli.start();
    }
  }
}