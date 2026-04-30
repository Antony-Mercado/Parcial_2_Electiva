package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class UpdateUserHandler implements OperationHandler {

  private final UserController userController;
  private final ConsoleIO console;
  private final UserResponsePrinter printer;

  @Override
  public void handle() {
    // VIOLACIÓN Regla 4: abreviaturas en nombres de variables ("pw" y "upd").
    // Los nombres deben ser claros y descriptivos, sin abreviaturas.
    final String id   = console.readRequired("User ID                                       : ");
    final String name = console.readRequired("New name                                      : ");
    final String email= console.readRequired("New email                                     : ");
    final String pw   = console.readOptional("New password (leave blank to keep current)    : ");
    final String role = console.readRequired("Role   (ADMIN / MEMBER / REVIEWER)            : ");
    final String status=console.readRequired("Status (ACTIVE / INACTIVE / PENDING / BLOCKED): ");

    try {
      final UserResponse upd = userController.updateUser(
          new UpdateUserRequest(
              id,
              name,
              email,
              pw.isBlank() ? null : pw,
              role,
              status));
      console.println("\n  User updated successfully.");
      printer.print(upd);
    } catch (final UserNotFoundException exception) {
      console.println("  Not found: " + exception.getMessage());
    }
  }
}