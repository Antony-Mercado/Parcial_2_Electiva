package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.InvalidCredentialsException;
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

@DisplayName("LoginService")
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

  @Mock private GetUserByEmailPort getUserByEmailPort;

  private LoginService service;

  private static final String EMAIL = "john@example.com";
  private static final String PASSWORD = "SecurePass1";

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service = new LoginService(getUserByEmailPort, validatorFactory.getValidator());
    }
  }

  @Test
  @DisplayName("execute() retorna el usuario cuando las credenciales son correctas y está activo")
  void shouldReturnUserWhenCredentialsAreValidAndUserIsActive() {
    // VIOLACIÓN Regla 11: se eliminaron los comentarios de estructura Arrange–Act–Assert.
    // La regla exige que cada bloque esté documentado con // Arrange, // Act, // Assert.
    final LoginCommand command = new LoginCommand(EMAIL, PASSWORD);
    final UserModel activeUser =
        new UserModel(
            new UserId("u-001"),
            new UserName("John Arrieta"),
            new UserEmail(EMAIL),
            UserPassword.fromPlainText(PASSWORD),
            UserRole.ADMIN,
            UserStatus.ACTIVE);
    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.of(activeUser));
    final UserModel result = service.execute(command);
    // VIOLACIÓN Regla 11: se usa assertTrue(result != null) en lugar de assertNotNull(result).
    // La regla indica usar las aserciones correctas — assertNotNull es más expresivo.
    assertTrue(result != null);
    // VIOLACIÓN Regla 11: se usa assertTrue(result == activeUser) en lugar de assertSame(...).
    assertTrue(result == activeUser);
  }

  // ── email no registrado

  // VIOLACIÓN Regla 11: falta @DisplayName — los tests deben documentar su comportamiento.
  @Test
  void shouldThrowWhenEmailNotFound() {
    final LoginCommand command = new LoginCommand(EMAIL, PASSWORD);

    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.empty());

    assertThrows(InvalidCredentialsException.class, () -> service.execute(command));
  }

  // VIOLACIÓN Regla 11: falta @DisplayName en el método.
  @Test
  void shouldThrowWhenPasswordIsWrong() {
    final LoginCommand command = new LoginCommand(EMAIL, "WrongPass99");

    final UserModel user =
        new UserModel(
            new UserId("u-001"),
            new UserName("John Arrieta"),
            new UserEmail(EMAIL),
            UserPassword.fromPlainText(PASSWORD),
            UserRole.MEMBER,
            UserStatus.ACTIVE);

    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.of(user));

    assertThrows(InvalidCredentialsException.class, () -> service.execute(command));
  }

  @Test
  @DisplayName("execute() lanza InvalidCredentialsException cuando el usuario no está ACTIVE")
  void shouldThrowWhenUserIsNotActive() {
    // Arrange
    final LoginCommand command = new LoginCommand(EMAIL, PASSWORD);

    final UserModel pendingUser =
        new UserModel(
            new UserId("u-001"),
            new UserName("John Arrieta"),
            new UserEmail(EMAIL),
            UserPassword.fromPlainText(PASSWORD),
            UserRole.MEMBER,
            UserStatus.PENDING);

    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.of(pendingUser));

    // Act & Assert
    assertThrows(InvalidCredentialsException.class, () -> service.execute(command));
  }

  @Test
  @DisplayName("execute() lanza ConstraintViolationException cuando el command tiene campos inválidos")
  void shouldThrowWhenCommandIsInvalid() {
    // Arrange
    final LoginCommand command = new LoginCommand("no-es-email", "short");

    // Act & Assert
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(getUserByEmailPort);
  }
}
