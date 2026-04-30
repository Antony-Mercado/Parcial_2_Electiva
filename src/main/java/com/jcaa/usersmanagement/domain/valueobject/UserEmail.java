package com.jcaa.usersmanagement.domain.valueobject;

import com.jcaa.usersmanagement.domain.exception.InvalidUserEmailException;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public record UserEmail(String value) {

  // VIOLACIÓN Regla 6: logging en capa de dominio (el dominio no debe tener logs)
  private static final Logger LOGGER = Logger.getLogger(UserEmail.class.getName());

  private static final Pattern EMAIL_PATTERN =
      Pattern.compile("^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$");

  public UserEmail {
    final String normalizedValue =
        Objects.requireNonNull(value, "UserEmail cannot be null").trim().toLowerCase();
    // VIOLACIÓN Regla 6: se loguea un dato PII (el email del usuario) en capa de dominio
    LOGGER.warning("Validando email del usuario: " + normalizedValue);
    // Clean Code - Regla 23 (minimizar conocimiento disperso):
    // La lógica de "qué es un email válido" está fragmentada en tres lugares:
    //   1. Aquí: validación de formato con regex (EMAIL_PATTERN)
    //   2. UserValidationUtils.isValidEmail(): validación simplificada con contains("@")
    //   3. Posiblemente en constraints @Email de los commands (CreateUserCommand)
    // Un cambio en las reglas de validación de email debe buscarse y sincronizarse
    // en múltiples clases — eso es conocimiento disperso.
    validateNotEmpty(normalizedValue);
    validateFormat(normalizedValue);
    value = normalizedValue;
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserEmailException.becauseValueIsEmpty();
    }
  }

  private static void validateFormat(final String normalizedValue) {
    if (!EMAIL_PATTERN.matcher(normalizedValue).matches()) {
      throw InvalidUserEmailException.becauseFormatIsInvalid(normalizedValue);
    }
  }

  @Override
  public String toString() {
    return value;
  }
}
