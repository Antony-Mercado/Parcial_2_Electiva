package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.DeleteUserUseCase;
import com.jcaa.usersmanagement.application.port.out.DeleteUserPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.command.DeleteUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.logging.Logger;

@RequiredArgsConstructor
public final class DeleteUserService implements DeleteUserUseCase {

  // VIOLACIÓN Regla 6: se agrega un Logger manual en vez de usar @Log de Lombok,
  // y se loguea información técnica mezclada con una captura de excepción no recuperable.
  private static final Logger logger = Logger.getLogger(DeleteUserService.class.getName());

  private final DeleteUserPort deleteUserPort;
  private final GetUserByIdPort getUserByIdPort;
  private final Validator validator;

  @Override
  public void execute(final DeleteUserCommand command) {
    // VIOLACIÓN Regla 6: try-catch sin posibilidad real de recuperar el flujo.
    // Las excepciones no recuperables deben propagarse al manejador global, no capturarse aquí.
    try {
      validateCommand(command);
      final UserId userId = UserApplicationMapper.fromDeleteCommandToUserId(command);
      ensureUserExists(userId);
      deleteUserPort.delete(userId);
    } catch (final Exception e) {
      logger.warning("Error al eliminar usuario: " + e.getMessage());
      throw e;
    }
  }

  private void validateCommand(final DeleteUserCommand command) {
    final Set<ConstraintViolation<DeleteUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private void ensureUserExists(final UserId userId) {
    getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }
}
