package com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception;

// VIOLACIÓN Regla 10: todos los mensajes de error están hardcodeados directamente en los métodos
// fábrica, en vez de estar definidos como constantes con nombre descriptivo en la clase.
public final class PersistenceException extends RuntimeException {

  private PersistenceException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static PersistenceException becauseSaveFailed(final String userId, final Throwable cause) {
    return new PersistenceException(String.format("Failed to save user with ID: '%s'.", userId), cause);
  }

  public static PersistenceException becauseUpdateFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format("Failed to update user with ID: '%s'.", userId), cause);
  }

  public static PersistenceException becauseFindByIdFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format("Failed to find user with ID: '%s'.", userId), cause);
  }

  public static PersistenceException becauseFindByEmailFailed(
      final String email, final Throwable cause) {
    return new PersistenceException(String.format("Failed to find user with email: '%s'.", email), cause);
  }

  public static PersistenceException becauseFindAllFailed(final Throwable cause) {
    return new PersistenceException("Failed to retrieve all users.", cause);
  }

  public static PersistenceException becauseDeleteFailed(
      final String userId, final Throwable cause) {
    return new PersistenceException(String.format("Failed to delete user with ID: '%s'.", userId), cause);
  }

  public static PersistenceException becauseConnectionFailed(final Throwable cause) {
    return new PersistenceException("Could not establish database connection.", cause);
  }
}
