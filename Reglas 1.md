# Guía de Buenas Prácticas de Codificación en Java

## Basada en Arquitectura Hexagonal y Clean Code

Esta guía presenta un conjunto de reglas y recomendaciones para el
desarrollo de software en Java, enfocadas en la construcción de sistemas
mantenibles, escalables y alineados con buenas prácticas de diseño,
Clean Code y arquitectura hexagonal.

------------------------------------------------------------------------

# 1. Arquitectura (Enfoque Hexagonal)

La arquitectura hexagonal organiza el sistema en capas claramente
separadas, donde el dominio es el núcleo y las dependencias siempre
apuntan hacia él.

## Capas principales

### Dominio (`domain`)

Contiene la lógica central del negocio: - Entidades - Agregados - Value
Objects - Servicios de dominio - Puertos (interfaces)

**Reglas clave:** - No debe depender de frameworks. - Solo se permite el
uso limitado de herramientas como Lombok o validaciones cuando sea
estrictamente necesario. - Debe ser independiente de infraestructura y
tecnología.

------------------------------------------------------------------------

### Aplicación / Core (`domain.core`)

Define la lógica de coordinación: - DTOs (commands, queries, request,
response) - Mappers - Servicios de aplicación - Interfaces de negocio

------------------------------------------------------------------------

### Puertos (`domain.port`)

-   **Entrada (in):** casos de uso expuestos
-   **Salida (out):** dependencias externas requeridas

------------------------------------------------------------------------

### Entrypoints (`entrypoint`)

Puntos de entrada al sistema: - Controladores REST - Consumidores de
colas - Schedulers

------------------------------------------------------------------------

### Adapters (`adapter`)

Interacción con sistemas externos: - Bases de datos - APIs externas -
Colas - Storage

------------------------------------------------------------------------

### Common (`common`)

Utilidades transversales: - Seguridad - Conversión - Telemetría -
Redacción de PII

------------------------------------------------------------------------

# 2. Modelado y Tipos

-   DTOs: usar `record` y builder cuando sea necesario
-   Entidades: clases con invariantes protegidas
-   Estados: usar `enum`
-   Value Objects: `record` con validación
-   Validar reglas del dominio en constructores

------------------------------------------------------------------------

# 3. Lombok y Validaciones

## Lombok

-   `@Builder`, `@Getter`, `@Setter`, `@RequiredArgsConstructor`,
    `@UtilityClass`
-   Evitar redundancia con `record`

## Validaciones

-   Solo en interfaces públicas
-   No en implementaciones
-   No usar `@Valid` en tipos simples
-   No personalizar mensajes

------------------------------------------------------------------------

# 4. Estilo y Naming

-   Nombres claros, sin abreviaturas
-   Evitar `==` en objetos, usar `Objects.equals`
-   No usar imports con `*`
-   Métodos sin estado → `static`
-   Clases utilitarias → `@UtilityClass`

------------------------------------------------------------------------

# 5. Manejo de Strings

-   Validar `null` y `blank` en dominio
-   Usar utilidades en otras capas
-   No aceptar strings vacíos
-   No retornar `null`

Alternativas: - `Optional` - Colecciones vacías - Excepciones

------------------------------------------------------------------------

# 6. Excepciones, Logging y Telemetría

## Excepciones

-   `try-catch` solo si hay recuperación
-   Manejo global de errores

## Logging

-   No loguear PII
-   Niveles:
   -   debug
   -   info
   -   error

## Ubicación

-   Dominio: sin logs
-   Entrypoints/adapters: sí logs

------------------------------------------------------------------------

# 7. Mappers y Serialización

-   Usar mappers entre capas
-   Usar MapStruct
-   No anotar dominio con JSON/XML
-   Evitar configuraciones innecesarias

------------------------------------------------------------------------

# 8. HTTP y Resiliencia

-   Usar `WebClient`
-   Configurar:
   -   timeouts
   -   retry
   -   circuit breaker
   -   trazabilidad
-   Centralizar configuración

------------------------------------------------------------------------

# 9. Buenas Prácticas de Diseño

-   Aplicar SOLID
-   Patrones:
   -   Strategy
   -   Builder
   -   Adapter
   -   Facade
   -   Factory

**Regla clave:** dependencias hacia el dominio

------------------------------------------------------------------------

# 10. Calidad del Código

-   Sin errores en SonarQube
-   No magic numbers
-   Usar constantes
-   No hardcodear textos

------------------------------------------------------------------------

# 11. Pruebas

-   Estructura AAA (Arrange--Act--Assert)
-   JUnit 5, Mockito, Java 21

## Buenas prácticas

-   Validar comportamiento real
-   Evitar pruebas triviales

------------------------------------------------------------------------

# Conclusión

Aplicar estas prácticas permite construir sistemas: - mantenibles -
desacoplados - claros - alineados con estándares profesionales
