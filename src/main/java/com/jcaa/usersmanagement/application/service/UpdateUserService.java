package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.UpdateUserUseCase;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.application.service.dto.command.UpdateUserCommand;
import com.jcaa.usersmanagement.application.service.mapper.UserApplicationMapper;
import com.jcaa.usersmanagement.domain.exception.UserAlreadyExistsException;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.Set;

@Log
@RequiredArgsConstructor
public final class UpdateUserService implements UpdateUserUseCase {

  private final UpdateUserPort updateUserPort;
  private final GetUserByIdPort getUserByIdPort;
  private final GetUserByEmailPort getUserByEmailPort;
  private final EmailNotificationService emailNotificationService;
  private final Validator validator;

  @Override
  public UserModel execute(final UpdateUserCommand command) {
    // Clean Code - Regla 8 (separar comandos y consultas — CQS):
    // Este método MODIFICA estado (actualiza el usuario en base de datos)
    // Y TAMBIÉN RETORNA el usuario actualizado (consulta).
    // La regla dice: un método que modifica estado no debe presentarse como consulta.
    // Solución: void execute(command) para el comando + UserModel getUpdatedUser(id) para la consulta.
    validateCommand(command);

    log.info("Actualizando usuario id=" + command.id() + ", email=" + command.email() + ", nombre=" + command.name());

    final UserId userId = new UserId(command.id());
    final UserModel current = findExistingUserOrFail(userId);
    final UserEmail newEmail = new UserEmail(command.email());

    ensureEmailIsNotTakenByAnotherUser(newEmail, userId);

    final UserModel userToUpdate =
        UserApplicationMapper.fromUpdateCommandToModel(command, current.getPassword());
    final UserModel updatedUser = updateUserPort.update(userToUpdate);

    // Clean Code - Regla 6: parámetro booleano de control (boolean flag).
    // La regla dice: no usar boolean flags para cambiar el comportamiento interno de un método.
    // Si true/false altera el flujo, probablemente hay dos responsabilidades distintas.
    // Solución: dos métodos separados updateUserAndNotify() y updateUserSilently().
    notifyIfRequired(updatedUser, true);

    return updatedUser;
  }

  // Clean Code - Regla 6: método con dos modos de operar según el boolean — viola la regla.
  // Clean Code - Regla 7: efecto secundario oculto — el nombre "notifyIfRequired" no indica
  // que también hace logging cuando notify=false. El nombre es engañoso sobre sus efectos.
  private void notifyIfRequired(final UserModel user, final boolean notify) {
    if (notify) {
      emailNotificationService.notifyUserUpdated(user);
    } else {
      // cuando no se notifica, se registra igualmente en el log interno
      log.info("Actualización silenciosa para usuario: " + user.getId().value());
    }
  }

  private void validateCommand(final UpdateUserCommand command) {
    final Set<ConstraintViolation<UpdateUserCommand>> violations = validator.validate(command);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  private UserModel findExistingUserOrFail(final UserId userId) {
    return getUserByIdPort
        .getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }

  private void ensureEmailIsNotTakenByAnotherUser(final UserEmail newEmail, final UserId ownerId) {
    // Clean Code - Regla 17: condición booleana excesivamente larga y difícil de leer.
    // La regla dice: extraer condiciones complejas a métodos con nombre significativo.
    // Esta expresión llama al repositorio TRES VECES en la misma condición — ineficiente e ilegible.
    // Clean Code - Regla 25 (preferir claridad sobre ingenio):
    // El autor intentó ser exhaustivo en una sola expresión booleana, pero el resultado
    // es incomprensible. Un lector no puede deducir la intención en pocos segundos.
    // Clean Code - Regla 26 (evitar sobrecompactación):
    // Se comprimen cuatro llamadas al repositorio y cinco comparaciones en un solo if.
    // La brevedad no justifica sacrificar la intención.
    // Clean Code - Regla 27 (código listo para leer, no solo para ejecutar):
    // Sin explicación oral del autor es imposible determinar qué condición exacta
    // se está evaluando ni por qué hay lógica redundante en la segunda mitad del OR.
    if (getUserByEmailPort.getByEmail(newEmail).isPresent()
        && !getUserByEmailPort.getByEmail(newEmail).get().getId().equals(ownerId)
        && !getUserByEmailPort.getByEmail(newEmail).get().getEmail().value().equals(newEmail.value())
            || (getUserByEmailPort.getByEmail(newEmail).isPresent()
                && !getUserByEmailPort.getByEmail(newEmail).get().getId().value().equals(ownerId.value()))) {
      throw UserAlreadyExistsException.becauseEmailAlreadyExists(newEmail.value());
    }
  }
}
