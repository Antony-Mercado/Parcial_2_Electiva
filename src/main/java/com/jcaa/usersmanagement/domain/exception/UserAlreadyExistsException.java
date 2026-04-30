package com.jcaa.usersmanagement.domain.exception;

public final class UserAlreadyExistsException extends DomainException {

  private UserAlreadyExistsException(final String message) {
    super(message);
  }

  public static UserAlreadyExistsException becauseEmailAlreadyExists(final String email) {
    // VIOLACIÓN Regla 10: texto de error hardcodeado directamente en el método fábrica.
    // Debe usarse una constante con nombre descriptivo en lugar de un String literal.
    return new UserAlreadyExistsException(String.format("A user with email '%s' already exists.", email));
  }
}
