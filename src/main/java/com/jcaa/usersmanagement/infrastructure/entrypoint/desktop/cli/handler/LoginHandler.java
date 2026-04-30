package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.handler;

import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.UserResponsePrinter;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller.UserController;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.LoginRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@RequiredArgsConstructor
public final class LoginHandler implements OperationHandler {

  private final UserController userController;
  private final ConsoleIO console;
  private final UserResponsePrinter printer;

  @Override
  public void handle() {
    final String email    = console.readRequired("Email   : ");
    final String password = console.readRequired("Password: ");
    try {
      final UserResponse user = userController.login(new LoginRequest(email, password));
      console.println("\n  Login successful. Welcome!");
      printer.print(user);
    } catch (final InvalidCredentialsException exception) {
      // VIOLACIÓN Regla 6: se loguea el email del usuario (PII) al registrar el fallo de login.
      // Los datos de negocio/cliente son PII y NO deben loguearse nunca.
      log.warning("Intento de login fallido para email: " + email);
      console.println("  Error: " + exception.getMessage());
    }
  }
}