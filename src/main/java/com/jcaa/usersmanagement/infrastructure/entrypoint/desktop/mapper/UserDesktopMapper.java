package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper;

import com.jcaa.usersmanagement.application.service.dto.command.CreateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.dto.command.LoginCommand;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.CreateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.LoginRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UpdateUserRequest;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto.UserResponse;

import java.util.List;

public final class UserDesktopMapper {

  private UserDesktopMapper() {
    // clase utilitaria: no se permite instanciar
  }

  // Regla 4 (Clean Code): los métodos públicos van primero; el auxiliar privado
  // aparece al final, cerca del método público que lo invoca.

  public static CreateUserCommand toCreateCommand(final CreateUserRequest request) {
    return new CreateUserCommand(
        request.id(), request.name(), request.email(), request.password(), request.role());
  }

  public static UpdateUserCommand toUpdateCommand(final UpdateUserRequest request) {
    return new UpdateUserCommand(
        request.id(),
        request.name(),
        request.email(),
        request.password(),
        request.role(),
        request.status());
  }

  public static DeleteUserCommand toDeleteCommand(final String id) {
    requireValidId(id);
    return new DeleteUserCommand(id);
  }

  public static GetUserByIdQuery toGetByIdQuery(final String id) {
    return new GetUserByIdQuery(id);
  }

  public static LoginCommand toLoginCommand(final LoginRequest request) {
    return new LoginCommand(request.email(), request.password());
  }

  public static UserResponse toResponse(final UserModel user) {
    return new UserResponse(
        user.getId().value(),
        user.getName().value(),
        user.getEmail().value(),
        user.getRole().name(),
        user.getStatus().name());
  }

  public static List<UserResponse> toResponseList(final List<UserModel> users) {
    return users.stream().map(UserDesktopMapper::toResponse).toList();
  }

  // Regla 21 (Clean Code): el auxiliar privado lanza una excepción directamente en lugar
  // de retornar un código de error (-1). El nombre expresa la intención ("requireValidId"),
  // y el contrato de salida es claro: o pasa sin error o lanza excepción.
  private static void requireValidId(final String id) {
    if (id == null || id.isBlank()) {
      throw new IllegalArgumentException("ID inválido");
    }
  }
}
