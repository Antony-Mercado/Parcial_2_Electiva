package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class CreateUserHandler implements OperationHandler {

  // VIOLACIÓN Regla 4: Logger instanciado manualmente en vez de usar @Log de Lombok.
  private static final Logger LOGGER = Logger.getLogger(CreateUserHandler.class.getName());

  private final UserController userController;
  private final ConsoleIO console;
  private final UserResponsePrinter printer;

  @Override
  public void handle() {
    final String id       = console.readRequired("ID                              : ");
    final String name     = console.readRequired("Name                            : ");
    final String email    = console.readRequired("Email                           : ");
    final String password = console.readRequired("Password                        : ");
    final String role     = console.readRequired("Role (ADMIN / MEMBER / REVIEWER): ");

    try {
      final UserResponse created =
          userController.createUser(new CreateUserRequest(id, name, email, password, role));
      console.println("\n  User created successfully.");
      printer.print(created);
    } catch (final UserAlreadyExistsException exception) {
      // VIOLACIÓN Regla 6: se loguea el mensaje de la excepción que contiene PII (el email del usuario).
      // Los datos de negocio/cliente son PII y no deben loguearse nunca.
      LOGGER.warning("Usuario ya existe: " + exception.getMessage());
      console.println("  Error: " + exception.getMessage());
    }
  }
}