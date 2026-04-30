package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.dto;

import lombok.Data;

// VIOLACIÓN Regla 2 (Reglas 1.md): se usa una clase mutable con @Data en lugar de record.
// Los DTOs de salida deben modelarse como record (inmutables) o usar @Builder(toBuilder=true).
// @Data genera setters, haciendo el DTO mutable, lo cual no es apropiado para un objeto de respuesta.
// Clean Code - Regla 15 (inmutabilidad como preferencia de diseño):
// Al igual que UserModel, este DTO expone setters públicos que permiten modificar
// cualquier campo desde cualquier parte del código después de construirlo:
//   response.setEmail("otro@email.com"); // nadie impide esto
// Un record o @Value eliminaría los setters y haría el objeto verdaderamente inmutable.
@Data
public class UserResponse {

  private String id;
  private String name;
  private String email;
  private String role;
  private String status;

  public UserResponse(
      final String id,
      final String name,
      final String email,
      final String role,
      final String status) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.role = role;
    this.status = status;
  }
}
