package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.enums.UserRole;

/**
 * Clean Code - Regla 13 (evitar clases utilitarias innecesarias):
 * Esta clase "Utils" agrupa métodos que en realidad pertenecen a sus respectivos objetos
 * de dominio (UserModel, UserRole, UserStatus) o a los servicios que los usan.
 *
 * La regla dice: no crear clases Utils/Helper/Manager sin una razón sólida.
 * La lógica de negocio vive en los objetos de negocio, no en utilitarios genéricos.
 * Una clase llamada "UserValidationUtils" es señal de:
 *   - diseño pobre
 *   - lógica mal ubicada
 *   - falta de encapsulación en dominio o servicios
 *
 * Clean Code - Regla 23 (minimizar conocimiento disperso):
 * Las reglas de validación de usuario están fragmentadas aquí en vez de estar
 * centralizadas en el propio UserModel o en un servicio de dominio dedicado.
 *
 * Clean Code - Regla 12 (alta cohesión real):
 * Esta clase mezcla responsabilidades que no pertenecen al mismo concepto:
 *   - Validación de estado (isUserActive)
 *   - Validación de rol (isAdmin)
 *   - Validación de formato de email (isValidEmail)
 *   - Validación de contraseña (isValidPassword)
 *   - Verificación de permisos con parámetros mixtos (canPerformAction)
 * Sus métodos no trabajan sobre un mismo concepto o responsabilidad — son un
 * "contenedor de cosas relacionadas vagamente". Eso es exactamente baja cohesión.
 */
public class UserValidationUtils {

  // Clean Code - Regla 13: la validación de si un usuario puede hacer login
  // debería vivir en UserModel.isAllowedToLogin() o en un servicio de dominio.
  public static boolean isUserActive(final UserModel user) {
    return user.getStatus() == UserStatus.ACTIVE;
  }

  // Clean Code - Regla 13: esta regla de negocio (qué roles son administradores)
  // debería encapsularse en UserRole o en un servicio de autorización, no aquí.
  public static boolean isAdmin(final UserModel user) {
    return user.getRole() == UserRole.ADMIN;
  }

  // Clean Code - Regla 11 (evitar duplicación): esta validación de email ya existe
  // en UserEmail y en UserEmail.isValidFormat() — se duplica lógica de dominio.
  // Clean Code - Regla 23: el conocimiento de qué es un email válido está disperso
  // entre UserEmail, UserValidationUtils y potencialmente otras clases.
  public static boolean isValidEmail(final String email) {
    if (email == null || email.isBlank()) {
      return false;
    }
    return email.contains("@") && email.contains(".");
  }

  // Clean Code - Regla 13: validación que pertenece al value object UserPassword.
  // Clean Code - Regla 18 (magic numbers): el número 8 es un magic number aquí —
  // ya tiene significado en UserPassword pero se repite sin constante.
  public static boolean isValidPassword(final String password) {
    return password != null && password.length() >= 8;
  }

  // Clean Code - Regla 20 (objeto antes que primitivo cuando el concepto lo merezca):
  // Este método recibe userId, email y status como String y int desnudos en lugar de
  // usar los tipos de dominio UserId, UserEmail y UserStatus.
  // El código pierde toda la protección de invariantes que ofrecen los value objects:
  // un id vacío, un email malformado o un status inválido pasarían desapercibidos.
  // La regla dice: encapsula conceptos como UserId, Email, Status con sus propios tipos.
  // Clean Code - Regla 5 (pocos parámetros): además recibe maxInactivityDays como
  // primitivo int suelto, que podría encapsularse en un objeto de política de acceso.
  public static boolean canPerformAction(
      final String userId,
      final String email,
      final String status,
      final int maxInactivityDays) {
    // Clean Code - Regla 17: condición larga y difícil de leer que debería extraerse.
    if (userId == null || userId.isBlank() || email == null || !email.contains("@")) {
      return false;
    }
    // Clean Code - Regla 18: "ACTIVE" y "PENDING" son literales mágicos —
    // deberían ser UserStatus.ACTIVE.name() o constantes con nombre descriptivo.
    return ("ACTIVE".equals(status) || "PENDING".equals(status)) && maxInactivityDays >= 0;
  }
}


