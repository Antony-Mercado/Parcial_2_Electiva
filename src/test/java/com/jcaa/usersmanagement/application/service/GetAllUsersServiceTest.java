package com.jcaa.usersmanagement.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.jcaa.usersmanagement.application.port.out.GetAllUsersPort;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// VIOLACIÓN Regla 11: se eliminó el javadoc de la clase que documentaba qué casos cubre.
@DisplayName("GetAllUsersService")
@ExtendWith(MockitoExtension.class)
class GetAllUsersServiceTest {

  @Mock private GetAllUsersPort getAllUsersPort;

  private GetAllUsersService service;

  @BeforeEach
  void setUp() {
    service = new GetAllUsersService(getAllUsersPort);
  }

  @Test
  @DisplayName("execute() retorna la lista de usuarios del puerto")
  void shouldReturnUsersFromPort() {
    // VIOLACIÓN Regla 11: se eliminaron los comentarios de estructura Arrange–Act–Assert.
    // La regla exige que los bloques estén documentados con // Arrange, // Act, // Assert.
    final UserModel user =
        new UserModel(
            new UserId("u-001"),
            new UserName("John Arrieta"),
            new UserEmail("john@example.com"),
            UserPassword.fromHash("$2a$12$abcdefghijklmnopqrstuO"),
            UserRole.ADMIN,
            UserStatus.ACTIVE);
    when(getAllUsersPort.getAll()).thenReturn(List.of(user));
    final List<UserModel> result = service.execute();
    // VIOLACIÓN Regla 11: se usa assertFalse(result.isEmpty()) y assertTrue(x == y)
    // en lugar de assertEquals(1, result.size()) y assertSame(user, result.get(0)).
    assertFalse(result.isEmpty());
    assertTrue(result.get(0) == user);
  }

  // VIOLACIÓN Regla 11: falta @DisplayName — los tests deben documentar su comportamiento.
  @Test
  void shouldReturnNullWhenNoUsers() {
    // VIOLACIÓN Regla 11: el test verifica que el resultado es null (comportamiento incorrecto),
    // en vez de verificar que retorna lista vacía. Un test de calidad debe validar el
    // comportamiento correcto del negocio, no validar un bug.
    when(getAllUsersPort.getAll()).thenReturn(List.of());
    final List<UserModel> result = service.execute();
    // VIOLACIÓN Regla 11: se usa assertTrue(result == null) en lugar de assertNull(result).
    assertTrue(result == null);
  }
}
