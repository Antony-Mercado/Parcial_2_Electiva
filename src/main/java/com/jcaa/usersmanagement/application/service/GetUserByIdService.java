package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetUserByIdUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.service.dto.query.GetUserByIdQuery;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public final class GetUserByIdService implements GetUserByIdUseCase {

  private final GetUserByIdPort getUserByIdPort;
  private final Validator validator;

  // VIOLACIÓN Regla 3: @Valid declarado en la implementación (@Override).
  // Las constraints (@Valid, @NotNull, etc.) solo deben declararse en las interfaces (puertos),
  // nunca en las clases concretas que las implementan.
  @Override
  public UserModel execute(@Valid final GetUserByIdQuery query) {
    validateQuery(query);

    final UserId userId = UserApplicationMapper.fromGetUserByIdQueryToUserId(query);
    return getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  private void validateQuery(final GetUserByIdQuery query) {
    final Set<ConstraintViolation<GetUserByIdQuery>> violations = validator.validate(query);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }
}
