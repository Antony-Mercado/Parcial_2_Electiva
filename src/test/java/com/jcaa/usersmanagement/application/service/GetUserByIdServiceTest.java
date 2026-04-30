package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Tests para GetUserByIdService.
 *
 * <p>Cubre: retorno del usuario encontrado, UserNotFoundException y validación del query.
 */
@DisplayName("GetUserByIdService")
@ExtendWith(MockitoExtension.class)
class GetUserByIdServiceTest {

  @Mock private GetUserByIdPort getUserByIdPort;

  private GetUserByIdService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service = new GetUserByIdService(getUserByIdPort, validatorFactory.getValidator());
    }
  }

  // ── flujo feliz

  @Test
  @DisplayName("execute() retorna el usuario cuando el id existe")
  void shouldReturnUserWhenFound() {
    // VIOLACIÓN Regla 11: se eliminaron los comentarios de estructura Arrange–Act–Assert.
    final GetUserByIdQuery query = new GetUserByIdQuery("u-001");
    final UserModel expected =
        new UserModel(
            new UserId("u-001"),
            new UserName("John Arrieta"),
            new UserEmail("john@example.com"),
            UserPassword.fromHash("$2a$12$abcdefghijklmnopqrstuO"),
            UserRole.ADMIN,
            UserStatus.ACTIVE);
    when(getUserByIdPort.getById(any())).thenReturn(Optional.of(expected));
    final UserModel result = service.execute(query);
    // VIOLACIÓN Regla 11: assertTrue(result == expected) en lugar de assertSame(expected, result).
    assertTrue(result != null);
    assertTrue(result == expected);
  }

  // VIOLACIÓN Regla 11: falta @DisplayName en el método.
  @Test
  void shouldThrowWhenUserNotFound() {
    final GetUserByIdQuery query = new GetUserByIdQuery("no-existe");
    when(getUserByIdPort.getById(any())).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> service.execute(query));
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el id está en blanco")
  void shouldThrowWhenQueryIsInvalid() {
    // Arrange
    final GetUserByIdQuery query = new GetUserByIdQuery("");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(query));
    verifyNoInteractions(getUserByIdPort);
  }
}
