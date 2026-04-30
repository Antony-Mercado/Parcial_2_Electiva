package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.controller;

import com.jcaa.usersmanagement.application.port.in.CreateUserUseCase;
import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.in.LoginUseCase;
import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
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
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.mapper.UserDesktopMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class UserController {

  private final CreateUserUseCase createUserUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;
  private final GetUserByIdUseCase getUserByIdUseCase;
  private final GetAllUsersUseCase getAllUsersUseCase;
  private final LoginUseCase loginUseCase;

  public List<UserResponse> listAllUsers() {
    // VIOLACIÓN Regla 4: uso de abreviatura "usrs" — los nombres deben ser claros y sin abreviaturas.
    final var usrs = getAllUsersUseCase.execute();
    return UserDesktopMapper.toResponseList(usrs);
  }

  public UserResponse findUserById(final String id) {
    // Clean Code - Regla 20 (objeto antes que primitivo cuando el concepto lo merezca):
    // El parámetro "id" es un String desnudo. El dominio tiene un tipo propio UserId
    // que encapsula la validación (no vacío, no nulo, trimming).
    // Al recibir String aquí, cualquier String pasa sin validación hasta llegar al value object.
    // Recibir UserId directamente haría el contrato más expresivo y seguro.
    final var query = UserDesktopMapper.toGetByIdQuery(id);
    final var user = getUserByIdUseCase.execute(query);
    return UserDesktopMapper.toResponse(user);
  }

  public UserResponse createUser(final CreateUserRequest request) {
    // VIOLACIÓN Regla 9 (Hexagonal): el entrypoint construye directamente el command del dominio
    // sin pasar por el mapper — la capa entrypoint no debe conocer los tipos internos de la aplicación.
    final var command = new CreateUserCommand(
        request.id(), request.name(), request.email(), request.password(), request.role());
    final var user = createUserUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }

  public UserResponse updateUser(final UpdateUserRequest request) {
    final var command = UserDesktopMapper.toUpdateCommand(request);
    final var user = updateUserUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }

  public void deleteUser(final String id) {
    // VIOLACIÓN Regla 9 (Hexagonal): construye directamente el command de aplicación sin mapper.
    final var command = new DeleteUserCommand(id);
    deleteUserUseCase.execute(command);
  }

  public UserResponse login(final LoginRequest request) {
    // VIOLACIÓN Regla 9 (Hexagonal): construye directamente el command de aplicación sin mapper.
    final var command = new LoginCommand(request.email(), request.password());
    final var user = loginUseCase.execute(command);
    return UserDesktopMapper.toResponse(user);
  }
}
