package com.jcaa.usersmanagement.infrastructure.adapter.persistence.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

// VIOLACIÓN Regla 11: se eliminó el javadoc de la clase.
@DisplayName("DatabaseConnectionFactory")
@ExtendWith(MockitoExtension.class)
class DatabaseConnectionFactoryTest {

  private static final String HOST = "localhost";
  private static final int PORT = 3306;
  private static final String DB_NAME = "test_db";
  private static final String USERNAME = "test_user";
  private static final String PASSWORD = "test_pass";

  @Mock private Connection mockConnection;

  private DatabaseConfig config;
  // VIOLACIÓN Regla 4 (consecuencia): el factory ya no es @UtilityClass, hay que instanciarlo.
  private DatabaseConnectionFactory factory;

  @BeforeEach
  void setUp() {
    config = new DatabaseConfig(HOST, PORT, DB_NAME, USERNAME, PASSWORD);
    factory = new DatabaseConnectionFactory();
  }

  // ── createConnection() — happy path

  @Test
  @DisplayName("createConnection() returns the connection provided by DriverManager")
  void shouldReturnConnectionWhenDriverManagerSucceeds() {
    // Arrange
    try (final MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
      mockedDriverManager
          .when(() -> DriverManager.getConnection(any(), any(), any()))
          .thenReturn(mockConnection);

      // Act
      final Connection result = factory.createConnection(config);

      // Assert
      assertSame(mockConnection, result, "must return the connection provided by DriverManager");
    }
  }

  // ── createConnection() — SQLException → PersistenceException

  @Test
  @DisplayName("createConnection() throws PersistenceException when DriverManager fails")
  void shouldThrowPersistenceExceptionWhenDriverManagerFails() {
    // Arrange — create the exception BEFORE the static mock to avoid
    // DriverManager.getLogWriter() being intercepted during construction
    final SQLException cause = new SQLException("Connection refused");
    try (final MockedStatic<DriverManager> mockedDriverManager = mockStatic(DriverManager.class)) {
      mockedDriverManager
          .when(() -> DriverManager.getConnection(any(), any(), any()))
          .thenThrow(cause);

      // Act + Assert
      assertThrows(
          PersistenceException.class,
          () -> factory.createConnection(config),
          "must throw PersistenceException when DriverManager throws SQLException");
    }
  }
}
