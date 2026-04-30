package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.SaveUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// VIOLACIÓN Regla 11: se eliminó @DisplayName de la clase y de los métodos.
// Los tests deben tener nombres descriptivos con @DisplayName para documentar el comportamiento.
@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

  @Mock private SaveUserPort saveUserPort;
  @Mock private GetUserByEmailPort getUserByEmailPort;
  @Mock private EmailNotificationService emailNotificationService;

  private CreateUserService service;

  @BeforeEach
  void setUp() {
    try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
      service =
          new CreateUserService(
              saveUserPort,
              getUserByEmailPort,
              emailNotificationService,
              validatorFactory.getValidator());
    }
  }

  @Test
  // VIOLACIÓN Regla 11: no hay comentarios de estructura Arrange–Act–Assert.
  // La regla exige que los bloques estén documentados con // Arrange, // Act, // Assert.
  void shouldSaveUserAndNotifyWhenEmailIsNew() {
    final CreateUserCommand command =
        new CreateUserCommand("u-01", "John Arrieta", "john@example.com", "Pass1234", "ADMIN");
    final UserModel savedUser =
        new UserModel(
            new UserId("u-01"),
            new UserName("John Arrieta"),
            new UserEmail("john@example.com"),
            UserPassword.fromPlainText("Pass1234"),
            UserRole.ADMIN,
            UserStatus.PENDING);
    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.empty());
    when(saveUserPort.save(any())).thenReturn(savedUser);
    final UserModel result = service.execute(command);
    // VIOLACIÓN Regla 11: se usa assertTrue(x != null) en lugar de assertNotNull(x).
    // La regla indica usar las últimas aserciones — assertNotNull es más expresivo y correcto.
    assertTrue(result != null);
    assertTrue(result.getId().value().equals("u-01"));
    verify(saveUserPort).save(any(UserModel.class));
    verify(emailNotificationService).notifyUserCreated(savedUser, "Pass1234");
  }

  @Test
  void shouldThrowWhenEmailAlreadyExists() {
    // VIOLACIÓN Regla 11: Arrange y Act–Assert mezclados sin separación ni comentarios AAA.
    final CreateUserCommand command =
        new CreateUserCommand("u-02", "Jane Doe", "jane@example.com", "Pass5678", "MEMBER");
    final UserModel existing =
        new UserModel(
            new UserId("u-99"),
            new UserName("Jane Doe"),
            new UserEmail("jane@example.com"),
            UserPassword.fromPlainText("OtraPass1"),
            UserRole.MEMBER,
            UserStatus.ACTIVE);
    when(getUserByEmailPort.getByEmail(any())).thenReturn(Optional.of(existing));
    assertThrows(UserAlreadyExistsException.class, () -> service.execute(command));
    verify(saveUserPort, never()).save(any());
    verify(emailNotificationService, never()).notifyUserCreated(any(), any());
  }

  @Test
  void shouldThrowWhenCommandIsInvalid() {
    final CreateUserCommand command =
        new CreateUserCommand("", "Jo", "not-an-email", "short", "ADMIN");
    assertThrows(ConstraintViolationException.class, () -> service.execute(command));
    verifyNoInteractions(saveUserPort, getUserByEmailPort, emailNotificationService);
  }
}
