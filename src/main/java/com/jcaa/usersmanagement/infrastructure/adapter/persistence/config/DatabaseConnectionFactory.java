package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// VIOLACIÓN Regla 4: clase con solo métodos estáticos que NO está anotada con @UtilityClass.
// Sin @UtilityClass, Lombok no genera el constructor privado y la clase puede instanciarse.
// Debería anotarse con @UtilityClass para evitar instanciación accidental.
public class DatabaseConnectionFactory {

  public Connection createConnection(final DatabaseConfig config) {
    // VIOLACIÓN Regla 4: método que no usa estado de instancia (solo usa el parámetro)
    // pero NO está declarado como static. Debería ser static.
    try {
      return DriverManager.getConnection(
          config.buildJdbcUrl(), config.username(), config.password());
    } catch (final SQLException exception) {
      throw PersistenceException.becauseConnectionFailed(exception);
    }
  }
}
