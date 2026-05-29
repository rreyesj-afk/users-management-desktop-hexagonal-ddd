package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.menu;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MainMenuOption {

  USER_MANAGEMENT(    1, "User Management"),
  EMPRESA_MANAGEMENT( 2, "Empresa Management"),
  EXIT(               0, "Exit");

  private final int number;
  private final String description;

  public static Optional<MainMenuOption> fromNumber(final int number) {
    return Arrays.stream(values()).filter(option -> option.number == number).findFirst();
  }
}