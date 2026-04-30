# users-management-hexagonal-java-bad-practices

> Aplicación de escritorio Java 17 con arquitectura hexagonal y DDD, usada como catálogo de malas prácticas de Clean Code para fines educativos.

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Maven](https://img.shields.io/badge/Maven-3.x-red?logo=apachemaven)
![JUnit 5](https://img.shields.io/badge/JUnit-5.11-green?logo=junit5)
![Mockito](https://img.shields.io/badge/Mockito-5.15-blue)
![Lombok](https://img.shields.io/badge/Lombok-1.18-pink)

---

## Funcionalidades del sistema

El sistema es una aplicación de escritorio por línea de comandos (CLI) que permite gestionar usuarios a través de un menú interactivo. Sus casos de uso son:

| # | Funcionalidad | Descripción |
|---|---|---|
| 1 | **Listar usuarios** | Obtiene y muestra todos los usuarios registrados en la base de datos |
| 2 | **Buscar usuario por ID** | Recupera un usuario específico mediante su identificador único |
| 3 | **Crear usuario** | Registra un nuevo usuario con validaciones de dominio y envía un correo de bienvenida |
| 4 | **Actualizar usuario** | Modifica los datos de un usuario existente y notifica el cambio por correo electrónico |
| 5 | **Eliminar usuario** | Borra un usuario del sistema verificando su existencia antes de la operación |
| 6 | **Login** | Autentica a un usuario validando credenciales y estado de cuenta |

---

## Arquitectura

El proyecto aplica **Arquitectura Hexagonal** (Ports & Adapters) combinada con principios de **Domain-Driven Design (DDD)**.

[Ver Digrama Estructura](lhttps://mermaid.live/view#pako:eNqVVdFumzAU_RXLVaVNImkICQl0q8QIlahIyIBE3cY0OWASVoKRIW2zql-xj9n79mMzBtJ06tqUF-zrc-49PtiXOxiQEEMVRgm5CVaIFsD74Kd-enwM3v_v4auG65mW7b6EDBKU5yMcgTiNKAJRnCTqkRh1FWkgBCQhVD2KokjIC0quMFsRxWF3UE9bN3FYrNRudnu6lwhlWZ2m25clvHgyDe51UPRsmpCsUZzWmTp9RZaVpzJ15H7Uk5_NtCA0xBTUkBDlzEaKtirog_4ujxIgCUWnB1lrTs4djfnrzHRv5mgvMfLNYklRtqp4X3z4Lx9-9dMwpjgoYpICyylFGBPP-cSw_D21zYn3bkFPzt7olglOgE5SpjtJMH3L2dpIm3qGw_D1qAKPt-5Hi8Ev0DUaozhhQ3fsTSuOn-I0PGi_2tQydU03__ycHLxXbTot1ewxH--yOsVT2_Hcb-aEQaeEFjmzqFI-yzHQUY5zJtlMC0wjFOC8Eu4aztzUDZeRXEyvY7bASTrFqMDg9y8wy8J6NMIJrkYWWcYp51dF7Zm3q8rGVVkHZySPC0K3rK6xLi3jiMaxRi9otc5Ao4NPdklfY-zIHpsT0z7YVIbXuFkN8SlPx_bIsBiGWUjHrHkkHDW3WWiOkg0G9uI7I-Q7o6kZCqB88w0LoN1uV_s1JrNx6bKRbtZ7cIckuCK4BSo2tTnGpV4Kq26tcRvgrNSU18ZxUdyoub0_4yUeBS711zh4bs0uXm5y_BLtfSZzsncKeLy-Ng-Hi0e5rIN06JbmGoc1W8DVCHXFqu82S408oZEhPMhkfbWBcV3C3Ba4fQLzrG6XUIBLGodQLegGC3CNKYuyKbzzUwB8WKzwGvtQZcMQ0Ssf-uk942Qo_UzIuqFRslmuoBqhJGezDb9MoxixM7jeRSn7QpjqZJMWUBUlngOqd_AWqi2pPVS6vZ7c6_clWRZFWYBbqPbb0lBRRIktiANFlHv3AvzBq4ptURGVwaDT7XUGQ0lWRAHisLyG4-rnx_-B938B9mYpGA)


### Capas

| Capa | Responsabilidad | Paquete |
|---|---|---|
| **Domain** | Entidades, Value Objects, enums, excepciones de dominio. Sin dependencias a frameworks | `domain/` |
| **Application** | Casos de uso, puertos (interfaces), servicios, DTOs, mappers | `application/` |
| **Entrypoint** | Controlador CLI, handlers de menú, DTOs y mappers de presentación | `infrastructure/entrypoint/` |
| **Adapter** | Repositorio MySQL (JDBC), adaptador de email SMTP (JavaMail) | `infrastructure/adapter/` |
| **Config** | Contenedor de dependencias manual, carga de propiedades | `infrastructure/config/` |

### Principios DDD aplicados

- **Value Objects** inmutables: `UserId`, `UserEmail`, `UserName`, `UserPassword`
- **Agregado raíz**: `UserModel` con invariantes protegidas en el dominio
- **Excepciones de dominio** expresivas con factory methods (`UserNotFoundException.becauseIdWasNotFound(...)`)
- **Puertos** como contratos de entrada (`CreateUserUseCase`) y salida (`SaveUserPort`, `EmailSenderPort`)

---

## Stack tecnológico

| Tecnología | Versión | Uso |
|---|---|---|
| Java | 17 | Lenguaje principal |
| Maven | 3.x | Gestión de dependencias y build |
| Lombok | 1.18.36 | Reducción de boilerplate (`@Data`, `@Builder`, `@Log`) |
| MySQL Connector/J | 9.3.0 | Acceso a base de datos vía JDBC |
| JavaMail (javax.mail) | 1.6.2 | Envío de correos SMTP |
| BCrypt | 0.10.2 | Hash seguro de contraseñas |
| Jakarta Validation | 3.0.2 | Validación de constraints en DTOs y commands |
| Hibernate Validator | 8.0.0 | Implementación de Jakarta Validation |
| JUnit Jupiter | 5.11.4 | Pruebas unitarias |
| Mockito | 5.15.2 | Mocks en pruebas |
| JaCoCo | 0.8.11 | Cobertura de código |
| SLF4J | 2.0.16 | Fachada de logging |

---

##  Propósito educativo: catálogo de malas prácticas

Este proyecto fue construido intencionalmente como un **catálogo de antipatrones y violaciones de Clean Code**. El código es completamente funcional y cuenta con 193 pruebas unitarias que pasan sin errores, pero cada clase contiene al menos una violación etiquetada con comentarios que explican el problema, su impacto y la solución correcta.

El objetivo es proporcionar ejemplos concretos y contextualizados de las prácticas que debemos **identificar y evitar** en el desarrollo de software profesional. Cada violación está anotada directamente en el código fuente con el formato:

```java
// Clean Code - Regla N (nombre de la regla):
// Descripción del problema.
// La regla dice: ...
// Solución: ...
```

### Reglas de Clean Code violadas

---

#### Tabla 1 — Violaciones según reglas de Hexagonal y estilo con Java

| # | Regla | Violación aplicada | Archivos con ejemplos |
|---|---|---|---|
| 1 | **Estructura — Arquitectura Hexagonal** | El dominio importa y depende de una clase de infraestructura (`UserEntity`). El entrypoint construye commands de aplicación directamente sin pasar por el mapper. | `UserModel`, `UserController` |
| 2 | **Modelado y tipos** | Los DTOs de salida usan `@Data` en lugar de `record`. El modelo de dominio usa `@Data` en lugar de `@Value`, exponiendo setters que violan sus invariantes. | `UserResponse`, `UserModel` |
| 3 | **Lombok y validaciones** | `@Valid` declarado sobre `@Override` en la implementación del caso de uso, no en la interfaz (puerto). | `GetUserByIdService` |
| 4 | **Estilo y naming** | Abreviaturas en variables (`v`, `r`, `usrs`, `pw`, `opt`); `== null` en lugar de `Objects.isNull`; imports con wildcard `*`; clases de utilidad sin `@UtilityClass`; nombre completo de clase dentro del código donde ya existe el import. | `ConsoleIO`, `UserId`, `UserName`, `UserPassword`, `UserRepositoryMySQL`, `UserPersistenceMapper`, `ValidatorProvider`, `JavaMailEmailSenderAdapter` |
| 5 | **Manejo de Strings** | Un método retorna `null` cuando la lista de usuarios está vacía en lugar de retornar una colección vacía u `Optional`. | `GetAllUsersService` |
| 6 | **Excepciones, logging y telemetría** | Log de dato PII (email del usuario) en capa de dominio. `try-catch` que captura excepciones no recuperables en lugar de dejarlas propagar al manejador global. Log de PII dentro de bloques `catch` en handlers del CLI. | `UserEmail`, `DeleteUserService`, `CreateUserHandler`, `LoginHandler` |
| 7 | **Mappers y (de)serialización** | Mapper de persistencia escrito manualmente en lugar de usar MapStruct. El entrypoint construye commands del dominio directamente, sin delegar al mapper de la capa. | `UserPersistenceMapper`, `UserController` |
| 9 | **Buenas prácticas de diseño** | Servicios con múltiples responsabilidades (validar, persistir, notificar, loguear en un mismo método). Dominio acoplado a infraestructura, violando la regla de dependencias hacia el centro. | `CreateUserService`, `LoginService`, `UserModel` |
| 10 | **Calidad** | Magic numbers sin constantes descriptivas (`8`, `12`, `3`). Textos de error y mensajes de UI hardcodeados como literales en lugar de constantes con nombre. | `UserPassword`, `UserName`, `ConsoleIO`, `UserNotFoundException`, `InvalidCredentialsException` |
| 11 | **Pruebas** | Tests sin estructura `// Arrange — Act — Assert`. Aserciones obsoletas o incorrectas (`assertTrue(result != null)` en lugar de `assertNotNull`; `assertTrue(result == activeUser)` en lugar de `assertSame`). Tests sin `@DisplayName`. | `LoginServiceTest` |

> La regla **8 (HTTP saliente / WebClient)** no aplica: este proyecto no realiza llamadas HTTP salientes.

---

#### Tabla 2 — Violaciones según `Clean Code

| # | Regla | Descripción de la violación | Archivos con ejemplos |
|---|---|---|---|
| 1 | **Una sola cosa por función** | Métodos que mezclan validación, construcción de dominio, persistencia, notificación y logging en un único bloque, sin posibilidad de describirse con un solo verbo. | `CreateUserService.execute`, `LoginService.getAndValidateUser` |
| 2 | **Funciones pequeñas** | Métodos que crecen hasta convertirse en mini-clases: hacen fetch, null-check, verificación de credenciales, validación de estado y retorno todo seguido. | `CreateUserService.execute`, `LoginService.getAndValidateUser` |
| 3 | **Un solo nivel de abstracción por función** | Métodos que combinan lógica de negocio de alto nivel con detalles técnicos de bajo nivel (I/O de classpath, manipulación de strings, construcción manual de objetos de dominio). | `CreateUserService.execute`, `EmailNotificationService.notifyUserCreated` |
| 4 | **Leer el código de arriba hacia abajo** | Método privado auxiliar (`renderTemplate`) declarado antes que los métodos públicos que lo invocan. Variables abreviadas que interrumpen la lectura secuencial natural. | `EmailNotificationService`, `ConsoleIO` |
| 5 | **Pocos parámetros por función** | Métodos que reciben cada campo del usuario como parámetro `String` o `int` primitivo suelto en lugar de encapsularlos en un objeto. | `UserRepositoryMySQL.saveWithFields`, `UserValidationUtils.canPerformAction` |
| 6 | **Evitar parámetros booleanos de control** | Parámetros `boolean` que bifurcan el flujo interno del método en dos comportamientos completamente distintos, señal clara de dos responsabilidades. | `UpdateUserService.notifyIfRequired`, `EmailNotificationService.sendNotificationWithFlag` |
| 7 | **Evitar efectos secundarios ocultos** | Métodos que realizan acciones inesperadas (logging de advertencia, cambios de flujo) que su nombre no comunica al llamador. | `UpdateUserService.notifyIfRequired`, `EmailNotificationService.sendOrLog` |
| 8 | **Separar comandos y consultas (CQS)** | Métodos que modifican estado (persistencia, actualización) y al mismo tiempo retornan el resultado como si fueran una simple consulta. | `LoginService.getAndValidateUser`, `UpdateUserService.execute` |
| 9 | **Código expresivo antes que comentarios** | Comentarios que tapan nombres pobres, bloques poco expresivos o diseño confuso en lugar de mejorar nombres y extraer funciones. | `CreateUserService.execute` |
| 10 | **Eliminar comentarios redundantes** | Comentarios que repiten literalmente lo que ya dice el código inmediato; magic numbers cuyo significado debe inferirse sin nombre descriptivo. | `CreateUserService`, `UserRepositoryMySQL`, `UserPassword`, `UserName` |
| 11 | **Evitar duplicación de conocimiento** | Lógica de orquestación idéntica (`loadTemplate → render → build → send`) repetida en dos métodos. Validaciones de negocio repetidas sin centralización. | `EmailNotificationService` (`notifyUserCreated` / `notifyUserUpdated`), `UserValidationUtils` |
| 12 | **Alta cohesión real** | Clases cuyos métodos trabajan sobre conceptos dispares y vagamente relacionados, sin un foco de negocio claro y único. | `LoginService`, `UserValidationUtils` |
| 13 | **Evitar clases utilitarias innecesarias** | Clases `Utils` con lógica que pertenece al dominio o a objetos de negocio; mappers manuales que deberían generarse automáticamente con MapStruct. | `UserValidationUtils`, `UserPersistenceMapper` |
| 14 | **Ley de Deméter** | Cadenas de llamadas que navegan a los internals de objetos ajenos en lugar de delegar la operación al propio objeto. | `LoginService` (`user.getPassword().verifyPlain()`), `UserPersistenceMapper` (`user.getId().value()`) |
| 15 | **Inmutabilidad como preferencia** | Modelo de dominio y DTO de respuesta usan `@Data`, que genera setters públicos y permite modificar el estado desde cualquier parte sin pasar por invariantes. | `UserModel` (debería ser `@Value`), `UserResponse` (debería ser `record`) |
| 16 | **Evitar condicionales repetitivas** | Cadena `if/else if` que crece con cada nuevo estado posible. Debería encapsularse en un `Map<String, String>` o en un método `getDisplayLabel()` del propio enum. | `UserResponsePrinter.getStatusLabel` |
| 17 | **Manejo limpio de condiciones** | Expresiones booleanas excesivamente largas, redundantes o crípticas que llaman al repositorio tres veces en una misma condición y ocultan la intención central. | `LoginService`, `UpdateUserService.ensureEmailIsNotTakenByAnotherUser` |
| 18 | **Evitar magic numbers y literales** | Valores especiales sin nombre (`8`, `12`, `3`, `"ACTIVE"`, `"PENDING"`) cuyo significado debe inferirse por contexto. | `UserPassword`, `UserName`, `UserValidationUtils` |
| 19 | **Evitar temporal coupling** | API que exige llamar a `init()` antes de cualquier operación pero el diseño no lo encapsula ni protege; orden de llamada implícito y frágil. | `UserRepositoryMySQL.init()`, `DependencyContainer` |
| 20 | **Objeto antes que primitivo** | `String` e `int` desnudos donde el dominio tiene tipos propios (`UserId`, `UserEmail`, `UserStatus`) con validaciones e invariantes encapsuladas. | `UserValidationUtils.canPerformAction`, `UserController.findUserById(String)` |
| 21 | **No usar códigos especiales de error** | Valores especiales (`-1`, `null`) para representar errores o ausencia en lugar de lanzar excepción o retornar `Optional` con semántica clara. | `UserApplicationMapper.roleToCode` (retorna `-1`), `GetAllUsersService` (retorna `null`) |
| 22 | **Código fácil de borrar y refactorizar** | Acoplamiento rígido a clases concretas que impide reemplazar cualquier componente sin editar múltiples puntos de entrada; dependencias no intercambiables. | `DependencyContainer`, `Main` |
| 23 | **Minimizar conocimiento disperso** | La regla de "qué es un email válido" está fragmentada en tres lugares distintos sin una fuente de verdad única. | `UserEmail` (regex completo), `UserValidationUtils.isValidEmail` (validación simplificada) |
| 24 | **Consistencia semántica** | El mismo concepto —"entrada de consola" o "email del usuario"— recibe nombres distintos en la misma clase sin ninguna justificación. | `UserApplicationMapper` (`correo` vs `correoElectronico`), `ConsoleIO` (`v` vs `r`) |
| 25 | **Preferir claridad sobre ingenio** | Expresiones que impresionan al autor pero castigan al lector; código que requiere descomponer mentalmente múltiples niveles de anidamiento para entender una intención simple. | `EmailNotificationService.notifyUserCreated`, `UpdateUserService.ensureEmailIsNotTakenByAnotherUser` |
| 26 | **Evitar sobrecompactación** | Múltiples decisiones comprimidas en una sola expresión o línea cuando separarlas en pasos nombrados mejoraría radicalmente la comprensión. | `EmailNotificationService.notifyUserCreated`, `UpdateUserService.ensureEmailIsNotTakenByAnotherUser` |
| 27 | **Código listo para leer** | Código funcionalmente correcto pero incomprensible sin explicación oral del autor; la intención no se deduce desde nombres, estructura ni responsabilidades. | `UserResponsePrinter.printSummary`, `UpdateUserService.ensureEmailIsNotTakenByAnotherUser` |

---

## Ejecución

### Prerrequisitos

- Java 17+
- Maven 3.8+
- MySQL 8+ con el schema aplicado (`src/main/resources/schema.sql`)
- Servidor SMTP accesible (o Mailtrap / MailHog para desarrollo)

### Configuración

Edita `src/main/resources/application.properties`:

```properties
db.host=localhost
db.port=3306
db.name=users_db
db.username=root
db.password=secret

smtp.host=smtp.example.com
smtp.port=587
smtp.username=user@example.com
smtp.password=secret
smtp.from.address=noreply@example.com
smtp.from.name=Users Management
```

### Compilar y ejecutar

```bash
mvn clean package -DskipTests
java -jar target/users-management-1.4.jar
```

### Tests

```bash
mvn test
```

>  193 tests · 0 fallos · 0 errores
