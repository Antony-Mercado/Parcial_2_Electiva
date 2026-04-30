package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.in.GetAllUsersUseCase;
import com.jcaa.usersmanagement.application.port.out.GetAllUsersPort;
import com.jcaa.usersmanagement.domain.model.UserModel;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class GetAllUsersService implements GetAllUsersUseCase {

  private final GetAllUsersPort getAllUsersPort;

  @Override
  public List<UserModel> execute() {
    final List<UserModel> users = getAllUsersPort.getAll();
    // VIOLACIÓN Regla 5 (Reglas 1.md): ningún método debe retornar null.
    // VIOLACIÓN Regla 21 (Clean Code — no retornar banderas de error):
    // null se usa aquí como "código especial de resultado vacío".
    // El contrato de salida no diferencia entre error, lista vacía y resultado válido:
    //   ¿null significa "ocurrió un error" o "no hay usuarios"?
    // Solución: retornar Collections.emptyList() cuando no hay usuarios.
    if (users.isEmpty()) {
      return null;
    }
    return users;
  }
}
