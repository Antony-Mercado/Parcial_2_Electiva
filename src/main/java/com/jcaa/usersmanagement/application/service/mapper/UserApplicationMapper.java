package com.jcaa.usersmanagement.application.service.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.enums.UserRole;
import com.jcaa.usersmanagement.domain.enums.UserStatus;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.domain.valueobject.UserName;
import com.jcaa.usersmanagement.domain.valueobject.UserPassword;
import java.util.Objects;

public class UserApplicationMapper {

  public static UserModel fromCreateCommandToModel(final CreateUserCommand command) {
    final String userId    = command.id();
    final String userName  = command.name();
    // Clean Code - Regla 24 (consistencia semántica):
    // El mismo concepto (email del usuario) se llama "correo" aquí
    // pero "correoElectronico" en fromUpdateCommandToModel, dentro de la MISMA clase.
    // La regla dice: las mismas ideas deben nombrarse igual en todo el proyecto.
    // No usar varios nombres para el mismo concepto sin justificación.
    final String correo    = command.email();
    final String userPass  = command.password();
    final String userRole  = command.role();

    return UserModel.create(
        new UserId(userId),
        new UserName(userName),
        new UserEmail(correo),
        UserPassword.fromPlainText(userPass),
        UserRole.fromString(userRole));
  }

  public static UserModel fromUpdateCommandToModel(
      final UpdateUserCommand command, final UserPassword currentPassword) {

    UserPassword passwordToUse;
    if (command.password() == null || command.password().isBlank()) {
      passwordToUse = currentPassword;
    } else {
      passwordToUse = UserPassword.fromPlainText(command.password());
    }

    // Clean Code - Regla 24: mismo concepto que "correo" de arriba, pero renombrado
    // sin razón a "correoElectronico". El lector no puede saber si son conceptos distintos.
    final String correoElectronico = command.email();

    // EFECTO CASCADA de la Regla 15 en UserModel:
    // Al usar @Data en vez de @Value, el modelo es mutable. El siguiente llamador
    // podría hacer userToUpdate.setStatus(BLOCKED) en cualquier momento después
    // de construirlo, sin pasar por ninguna regla de dominio.
    return new UserModel(
        new UserId(command.id()),
        new UserName(command.name()),
        new UserEmail(correoElectronico),
        passwordToUse,
        UserRole.fromString(command.role()),
        UserStatus.fromString(command.status()));
  }

  public static UserId fromGetUserByIdQueryToUserId(final GetUserByIdQuery query) {
    return new UserId(query.id());
  }

  public static UserId fromDeleteCommandToUserId(final DeleteUserCommand command) {
    return new UserId(command.id());
  }

  // Clean Code - Regla 21 (no retornar banderas de error):
  // Este método retorna 1, 2, 3 o -1 como códigos de resultado para representar roles.
  // La regla dice: no usar valores especiales (-1, null, "ERROR", false) para señalar errores.
  // El contrato de salida NO diferencia ausencia, falla y éxito:
  //   - ¿Qué significa -1? ¿Error de parseo? ¿Rol desconocido? ¿No autorizado?
  //   - El llamador DEBE recordar qué valor representa cada caso — frágil y opaco.
  // Solución: lanzar IllegalArgumentException o usar Optional<Integer> con semántica clara.
  public static int roleToCode(final String role) {
    if (Objects.isNull(role) || role.isBlank()) {
      return -1;
    }
    if ("ADMIN".equalsIgnoreCase(role)) {
      return 1;
    } else if ("MEMBER".equalsIgnoreCase(role)) {
      return 2;
    } else if ("REVIEWER".equalsIgnoreCase(role)) {
      return 3;
    }
    return -1;
  }
}
