package com.jcaa.usersmanagement.application.service;

import com.jcaa.usersmanagement.application.port.out.EmailSenderPort;
import com.jcaa.usersmanagement.domain.exception.EmailSenderException;
import com.jcaa.usersmanagement.domain.model.EmailDestinationModel;
import com.jcaa.usersmanagement.domain.model.UserModel;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;

@Log
@RequiredArgsConstructor
public final class EmailNotificationService {

  private static final String SUBJECT_CREATED = "Tu cuenta ha sido creada — Gestión de Usuarios";
  private static final String SUBJECT_UPDATED =
      "Tu cuenta ha sido actualizada — Gestión de Usuarios";

  private static final String TOKEN_NAME     = "name";
  private static final String TOKEN_EMAIL    = "email";
  private static final String TOKEN_PASSWORD = "password";
  private static final String TOKEN_ROLE     = "role";
  private static final String TOKEN_STATUS   = "status";

  private final EmailSenderPort emailSenderPort;

  public void notifyUserCreated(final UserModel user, final String plainPassword) {
    // Clean Code - Regla 25 (claridad sobre ingenio) y Regla 26 (evitar sobrecompactación):
    // Se comprime toda la operación en una cadena de llamadas anidadas en una sola expresión.
    // Aunque "funciona", sacrifica completamente la legibilidad.
    // Clean Code - Regla 3 (un solo nivel de abstracción por función):
    // Esta línea mezcla niveles de abstracción radicalmente distintos en una sola expresión:
    //   - Alto nivel:  "notificar al usuario creado"
    //   - Medio nivel: buildDestination(), sendOrLog()
    //   - Bajo nivel:  loadTemplate() (I/O de classpath), renderTemplate() (manipulación de Strings)
    // La regla dice: dentro del mismo método no deben convivir reglas de negocio
    // con detalles técnicos de I/O, parseo o formateo de texto.
    // Clean Code - Regla 11 (evitar duplicación): la construcción de tokens del mapa
    // es idéntica a la de notifyUserUpdated — debería centralizarse.
    sendOrLog(buildDestination(user, SUBJECT_CREATED,
        renderTemplate(loadTemplate("user-created.html"),
            Map.of(TOKEN_NAME, user.getName().value(), TOKEN_EMAIL, user.getEmail().value(),
                TOKEN_PASSWORD, plainPassword, TOKEN_ROLE, user.getRole().name()))));
  }

  public void notifyUserUpdated(final UserModel user) {
    // Clean Code - Regla 11 (evitar duplicación): misma estructura que notifyUserCreated —
    // loadTemplate → renderTemplate → buildDestination → sendOrLog.
    // Esta lógica de orquestación debería extraerse a un método genérico privado.
    // Clean Code - Regla 25 y 26: misma sobrecompactación que arriba.
    sendOrLog(buildDestination(user, SUBJECT_UPDATED,
        renderTemplate(loadTemplate("user-updated.html"),
            Map.of(TOKEN_NAME, user.getName().value(), TOKEN_EMAIL, user.getEmail().value(),
                TOKEN_ROLE, user.getRole().name(), TOKEN_STATUS, user.getStatus().name()))));
  }

  // Clean Code - Regla 6 (evitar parámetros booleanos de control):
  // El boolean includePassword cambia completamente el comportamiento del método:
  // - true  → usa plantilla de creación con contraseña
  // - false → usa plantilla de actualización sin contraseña
  // La regla dice: si un boolean altera el flujo, probablemente hay dos responsabilidades.
  // Solución: dos métodos separados notifyUserCreated() y notifyUserUpdated() (que ya existen).
  public void sendNotificationWithFlag(
      final UserModel user, final boolean includePassword, final String plainPassword) {
    if (includePassword) {
      notifyUserCreated(user, plainPassword);
    } else {
      notifyUserUpdated(user);
    }
  }

  private static EmailDestinationModel buildDestination(
      final UserModel user, final String subject, final String body) {
    return new EmailDestinationModel(
        user.getEmail().value(), user.getName().value(), subject, body);
  }

  private String loadTemplate(final String templateName) {
    final String path = "/templates/" + templateName;
    try (InputStream inputStream = openResourceStream(path)) {
      if (Objects.isNull(inputStream)) {
        throw EmailSenderException.becauseSendFailed(
            new IllegalStateException("Template not found: " + path));
      }
      return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    } catch (final IOException ioException) {
      throw EmailSenderException.becauseSendFailed(ioException);
    }
  }

  InputStream openResourceStream(final String path) {
    return getClass().getResourceAsStream(path);
  }

  // VIOLACIÓN Regla 4: método privado que no usa estado de instancia (no usa this ni campos)
  // pero NO está declarado como static. La regla dice: métodos privados sin estado deben ser static.
  private String renderTemplate(String template, final Map<String, String> values) {
    String result = template;
    for (final Map.Entry<String, String> tokenEntry : values.entrySet()) {
      final String token = "{{" + tokenEntry.getKey() + "}}";
      result = result.replace(token, tokenEntry.getValue());
    }
    return result;
  }

  // Clean Code - Regla 7 (evitar efectos secundarios ocultos):
  // El nombre "sendOrLog" promete dos cosas (enviar o loguear), pero ninguna de las
  // dos describe el comportamiento real completo: en el flujo exitoso NO loguea nada,
  // y en el fallido loguea Y re-lanza la excepción.
  // Los llamadores (notifyUserCreated, notifyUserUpdated) creen que solo "envían un correo",
  // pero en realidad también producen un log de advertencia de forma inesperada.
  // La regla dice: una función no debe realizar acciones inesperadas además de lo que
  // su nombre promete.
  private void sendOrLog(final EmailDestinationModel destination) {
    try {
      emailSenderPort.send(destination);
    } catch (final EmailSenderException senderException) {
      log.log(
          Level.WARNING,
          "[EmailNotificationService] No se pudo enviar correo a: {0}. Causa: {1}",
          new Object[] {destination.getDestinationEmail(), senderException.getMessage()});
      throw senderException;
    }
  }
}
