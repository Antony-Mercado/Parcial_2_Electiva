package com.jcaa.usersmanagement.infrastructure.adapter.persistence.repository;

import com.jcaa.usersmanagement.application.port.out.DeleteUserPort;
import com.jcaa.usersmanagement.application.port.out.GetAllUsersPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByEmailPort;
import com.jcaa.usersmanagement.application.port.out.GetUserByIdPort;
import com.jcaa.usersmanagement.application.port.out.SaveUserPort;
import com.jcaa.usersmanagement.application.port.out.UpdateUserPort;
import com.jcaa.usersmanagement.domain.exception.UserNotFoundException;
import com.jcaa.usersmanagement.domain.model.UserModel;
import com.jcaa.usersmanagement.domain.valueobject.UserEmail;
import com.jcaa.usersmanagement.domain.valueobject.UserId;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.dto.UserPersistenceDto;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.exception.PersistenceException;
import com.jcaa.usersmanagement.infrastructure.adapter.persistence.mapper.UserPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

// VIOLACIÓN Regla 4: import con wildcard (*) — se deben declarar imports específicos,
// nunca usar comodines porque ocultan qué clases se están usando realmente.
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Log
@RequiredArgsConstructor
public final class UserRepositoryMySQL
    implements SaveUserPort,
        UpdateUserPort,
        GetUserByIdPort,
        GetUserByEmailPort,
        GetAllUsersPort,
        DeleteUserPort {

  // VIOLACIÓN Regla 4 (consecuencia): el mapper ya no es @UtilityClass,
  // por lo que se instancia directamente aquí en vez de usarse como clase utilitaria.
  private final UserPersistenceMapper persistenceMapper = new UserPersistenceMapper();

  private static final String SQL_INSERT =
      "INSERT INTO users "
      + "(id, name, email, password, role, status, created_at, updated_at) "
      + "VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";

  private static final String SQL_UPDATE =
      "UPDATE users SET name = ?, email = ?, password = ?, role = ?, status = ?, updated_at = NOW() "
      + "WHERE id = ?";

  private static final String SQL_SELECT_BY_ID =
      "SELECT id, name, email, password, role, status, created_at, updated_at "
      + "FROM users "
      + "WHERE id = ? LIMIT 1";

  private static final String SQL_SELECT_BY_EMAIL =
      "SELECT id, name, email, password, role, status, created_at, updated_at "
      + "FROM users "
      + "WHERE email = ? LIMIT 1";

  private static final String SQL_SELECT_ALL =
      "SELECT id, name, email, password, role, status, created_at, updated_at "
      + "FROM users "
      + "ORDER BY name ASC";

  private static final String SQL_DELETE =
        "DELETE FROM users "
        + "WHERE id = ?";

  private final Connection connection;

  // Clean Code - Regla 19 (temporal coupling): se agrega un flag de inicialización.
  // El consumidor DEBE llamar a init() antes de usar el repositorio, pero el diseño
  // no lo hace evidente ni lo encapsula — se llama a métodos en orden implícito frágil.
  // Si alguien usa el repositorio sin llamar init() el comportamiento es impredecible.
  private boolean initialized = false;

  // Clean Code - Regla 19: si el orden init() → operaciones es obligatorio, el diseño
  // debería proteger al consumidor encapsulando ese orden. Ahora es fácil usarlo mal:
  //   service.init();
  //   service.load();
  //   service.process(); // ← ¿qué pasa si alguien llama esto sin init() primero?
  public void init() {
    this.initialized = true;
  }

  @Override
  public UserModel save(final UserModel user) {
    // Clean Code - Regla 10: comentarios redundantes que repiten lo que ya dice el código.
    // transformar el modelo de dominio a DTO de persistencia
    final UserPersistenceDto dto = persistenceMapper.fromModelToDto(user);
    // ejecutar la consulta de inserción en la base de datos
    executeSave(dto);
    // buscar y retornar el usuario recién guardado
    return findByIdOrFail(user.getId());
  }

  // Clean Code - Regla 5 (pocos parámetros): método alternativo de guardado que recibe
  // cada campo como parámetro primitivo separado en vez de encapsularlos en un objeto.
  // La regla dice: si una función necesita muchos datos relacionados, encapsúlalos en un objeto.
  // createUser(String name, String email, ...) es señal clara de diseño mejorable.
  public UserModel saveWithFields(
      final String id,
      final String name,
      final String email,
      final String password,
      final String role,
      final String status) {
    // Clean Code - Regla 10: comentario redundante — la línea siguiente ya es clara.
    // verificar que todos los parámetros tengan valor
    if (id == null || name == null || email == null || password == null || role == null || status == null) {
      throw new IllegalArgumentException("Todos los campos son obligatorios");
    }
    // Clean Code - Regla 10: otro comentario redundante.
    // construir y guardar el modelo
    throw new UnsupportedOperationException("Usar save(UserModel) en su lugar.");
  }

  @Override
  public UserModel update(final UserModel user) {
    final UserPersistenceDto dto = persistenceMapper.fromModelToDto(user);
    executeUpdate(dto);
    return findByIdOrFail(user.getId());
  }

  @Override
  public Optional<UserModel> getById(final UserId userId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
      statement.setString(1, userId.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(persistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindByIdFailed(userId.value(), exception);
    }
  }

  @Override
  public Optional<UserModel> getByEmail(final UserEmail email) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_BY_EMAIL)) {
      statement.setString(1, email.value());
      final ResultSet resultSet = statement.executeQuery();
      if (!resultSet.next()) {
        return Optional.empty();
      }
      return Optional.of(persistenceMapper.fromResultSetToModel(resultSet));
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindByEmailFailed(email.value(), exception);
    }
  }

  @Override
  public List<UserModel> getAll() {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_SELECT_ALL)) {
      final ResultSet resultSet = statement.executeQuery();
      return persistenceMapper.fromResultSetToModelList(resultSet);
    } catch (final SQLException exception) {
      throw PersistenceException.becauseFindAllFailed(exception);
    }
  }

  @Override
  public void delete(final UserId userId) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_DELETE)) {
      statement.setString(1, userId.value());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseDeleteFailed(userId.value(), exception);
    }
  }

  private void executeSave(final UserPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
      statement.setString(1, dto.id());
      statement.setString(2, dto.name());
      statement.setString(3, dto.email());
      statement.setString(4, dto.password());
      statement.setString(5, dto.role());
      statement.setString(6, dto.status());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseSaveFailed(dto.id(), exception);
    }
  }

  private void executeUpdate(final UserPersistenceDto dto) {
    try (final PreparedStatement statement = connection.prepareStatement(SQL_UPDATE)) {
      statement.setString(1, dto.name());
      statement.setString(2, dto.email());
      statement.setString(3, dto.password());
      statement.setString(4, dto.role());
      statement.setString(5, dto.status());
      statement.setString(6, dto.id());
      statement.executeUpdate();
    } catch (final SQLException exception) {
      throw PersistenceException.becauseUpdateFailed(dto.id(), exception);
    }
  }

  private UserModel findByIdOrFail(final UserId userId) {
    return getById(userId)
        .orElseThrow(() -> UserNotFoundException.becauseIdWasNotFound(userId.value()));
  }
}
