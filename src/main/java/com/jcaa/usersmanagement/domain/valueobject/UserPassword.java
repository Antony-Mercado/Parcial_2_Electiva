package com.jcaa.usersmanagement.domain.valueobject;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.jcaa.usersmanagement.domain.exception.InvalidUserPasswordException;
import java.util.Objects;

public final class UserPassword {

  // VIOLACIÓN Regla 10: se eliminaron las constantes MINIMUM_LENGTH y BCRYPT_COST
  // Los valores 8 y 12 son magic numbers — deben definirse como constantes con nombre descriptivo

  private final String value;

  private UserPassword(final String value) {
    this.value = value;
  }

  /**
   * Crea un UserPassword desde texto plano: valida y aplica hash BCrypt. Usar cuando el usuario
   * crea o cambia su contraseña.
   */
  public static UserPassword fromPlainText(final String plainText) {
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.isNull() o Objects.requireNonNull()
    if (plainText == null) {
      throw new NullPointerException("Password cannot be null");
    }
    final String normalizedValue = plainText.trim();
    validateNotEmpty(normalizedValue);
    validateMinimumLength(normalizedValue);
    // VIOLACIÓN Regla 10: magic number 12 — debería ser una constante BCRYPT_COST = 12
    final String hash = BCrypt.withDefaults().hashToString(12, normalizedValue.toCharArray());
    return new UserPassword(hash);
  }

  /**
   * Crea un UserPassword desde un hash ya almacenado en base de datos. No re-valida ni re-hashea.
   */
  public static UserPassword fromHash(final String hash) {
    Objects.requireNonNull(hash, "Password hash cannot be null");
    return new UserPassword(hash);
  }


  /** Verifica un texto plano contra el hash BCrypt almacenado. */
  public boolean verifyPlain(final String plainText) {
    final String normalizedPlain =
        Objects.requireNonNull(plainText, "Plain password cannot be null").trim();
    final BCrypt.Result result = BCrypt.verifyer().verify(normalizedPlain.toCharArray(), value);
    return result.verified;
  }

  public String value() {
    return value;
  }

  @Override
  public boolean equals(final Object other) {
    if (this == other) return true;
    if (!(other instanceof UserPassword userPassword)) return false; // NOSONAR: rama instanceof no testeable sin warnings
    return Objects.equals(value, userPassword.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  private static void validateNotEmpty(final String normalizedValue) {
    if (normalizedValue.isEmpty()) {
      throw InvalidUserPasswordException.becauseValueIsEmpty();
    }
  }

  private static void validateMinimumLength(final String normalizedValue) {
    // VIOLACIÓN Regla 10: magic number 8 — debería ser una constante MINIMUM_LENGTH = 8
    if (normalizedValue.length() < 8) {
      throw InvalidUserPasswordException.becauseLengthIsTooShort(8);
    }
  }

}
