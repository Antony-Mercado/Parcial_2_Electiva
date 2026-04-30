package com.jcaa.usersmanagement.domain.valueobject;

import static org.junit.jupiter.api.Assertions.*;

import com.jcaa.usersmanagement.domain.exception.InvalidUserIdException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

// VIOLACIÓN Regla 11: se eliminó @DisplayName de la clase y de todos los métodos.
// Los tests deben tener nombres descriptivos con @DisplayName para documentar el comportamiento.
class UserIdTest {

  @ParameterizedTest
  @ValueSource(strings = {" user123 ", "  user123  ", "user123\t"})
  void shouldCreateUserIdWithTrimmedValue(String input) {
    // VIOLACIÓN Regla 11: se eliminaron los comentarios Arrange–Act–Assert.
    final String correctUserId = "user123";
    final UserId userId = new UserId(input);
    // VIOLACIÓN Regla 11: se usa assertTrue(x.equals(y)) en lugar de assertEquals(x, y).
    assertTrue(correctUserId.equals(userId.toString()));
  }

  @Test
  void shouldThrowNullPointerExceptionWhenUserIdIsNull() {
    assertThrows(NullPointerException.class, () -> new UserId(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   ", "\t", "\n", "\r", "\f", "\b"})
  void shouldThrowIllegalArgumentExceptionWhenUserIdIsEmpty(String input) {
    assertThrows(InvalidUserIdException.class, () -> new UserId(input));
  }
}
