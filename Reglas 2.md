# Guía de Reglas de Clean Code para Java

## Enfoque en Diseño de Funciones, Legibilidad y Mantenibilidad

Esta guía complementa un conjunto de buenas prácticas de desarrollo,
enfocándose específicamente en la calidad interna del código: funciones,
legibilidad, cohesión, diseño y expresividad.

------------------------------------------------------------------------

# 1. Funciones pequeñas y de una sola responsabilidad

Las funciones deben estar diseñadas para realizar una única tarea clara.

### Principios clave

-   Cada función hace una sola cosa.
-   No mezcla validación, transformación, persistencia, logging o
    notificación.
-   Puede describirse con un único verbo claro.
-   Si contiene múltiples bloques diferenciados, debe dividirse.

### Problema típico

Funciones que, aunque funcionales, concentran demasiadas
responsabilidades, dificultando su lectura, prueba y mantenimiento.

------------------------------------------------------------------------

# 2. Funciones cortas

Las funciones deben ser lo suficientemente pequeñas como para entenderse
rápidamente.

### Principios clave

-   Evitar métodos largos que actúan como "mini-clases".
-   Deben leerse completas sin perder el contexto.
-   Su intención debe entenderse en pocos segundos.

------------------------------------------------------------------------

# 3. Un solo nivel de abstracción por función

Una función no debe mezclar distintos niveles de detalle.

### Principios clave

-   No combinar lógica de negocio con detalles técnicos.
-   Separar reglas de alto nivel de operaciones de bajo nivel (formato,
    parsing, logging).

### Problema típico

Funciones que combinan lógica de negocio con operaciones técnicas,
dificultando la comprensión.

------------------------------------------------------------------------

# 4. Lectura secuencial del código

El código debe poder leerse de forma natural de arriba hacia abajo.

### Principios clave

-   Métodos principales primero.
-   Métodos auxiliares cerca de su uso.
-   Flujo lógico claro y progresivo.

------------------------------------------------------------------------

# 5. Pocos parámetros por función

Las funciones deben recibir la menor cantidad posible de parámetros.

### Principios clave

-   Encapsular datos relacionados en objetos.
-   Evitar listas largas de parámetros primitivos.

------------------------------------------------------------------------

# 6. Evitar parámetros booleanos de control

Los parámetros booleanos que cambian el comportamiento indican un diseño
mejorable.

### Principios clave

-   Evitar funciones con múltiples modos de operación.
-   Preferir métodos separados con nombres claros.

------------------------------------------------------------------------

# 7. Evitar efectos secundarios ocultos

Las funciones deben hacer exactamente lo que su nombre indica.

### Principios clave

-   No realizar acciones adicionales inesperadas.
-   Evitar modificaciones de estado no evidentes.

------------------------------------------------------------------------

# 8. Separar comandos y consultas

Un método debe hacer una sola de estas cosas: - Consultar información -
Modificar estado

Nunca ambas.

------------------------------------------------------------------------

# 9. Código expresivo antes que comentarios

El código debe ser autoexplicativo.

### Principios clave

-   Preferir nombres claros antes que comentarios.
-   Usar comentarios solo cuando aporten contexto no evidente.

------------------------------------------------------------------------

# 10. Eliminar comentarios innecesarios

### Principios clave

-   Evitar comentarios redundantes.
-   Eliminar comentarios desactualizados.
-   No usar código comentado como historial.

------------------------------------------------------------------------

# 11. Evitar duplicación de conocimiento

No repetir lógica de negocio en múltiples lugares.

### Principios clave

-   Centralizar reglas.
-   Evitar duplicar validaciones o condiciones.

------------------------------------------------------------------------

# 12. Alta cohesión en clases

Cada clase debe tener un propósito claro.

### Principios clave

-   Métodos relacionados con una misma responsabilidad.
-   Evitar clases con funciones inconexas.

------------------------------------------------------------------------

# 13. Evitar clases utilitarias innecesarias

### Principios clave

-   No usar clases tipo `Utils` sin justificación.
-   Ubicar la lógica en el contexto adecuado.

------------------------------------------------------------------------

# 14. Ley de Deméter

Reducir el acoplamiento entre objetos.

### Principios clave

-   Evitar encadenamientos profundos.
-   No exponer estructura interna.

------------------------------------------------------------------------

# 15. Preferir inmutabilidad

### Principios clave

-   Evitar setters innecesarios.
-   Controlar cambios de estado.

------------------------------------------------------------------------

# 16. Reducir uso de condicionales complejos

### Principios clave

-   Evitar cadenas largas de `if/else`.
-   Evaluar uso de polimorfismo.

------------------------------------------------------------------------

# 17. Manejo limpio de condiciones

### Principios clave

-   Condiciones claras y legibles.
-   Evitar expresiones booleanas complejas.
-   Usar métodos descriptivos.

------------------------------------------------------------------------

# 18. Evitar valores mágicos

### Principios clave

-   Usar constantes con significado.
-   Evitar valores sin contexto.

------------------------------------------------------------------------

# 19. Evitar acoplamiento temporal

### Principios clave

-   No obligar a usar métodos en orden específico sin protección.
-   Diseñar APIs seguras.

------------------------------------------------------------------------

# 20. Usar tipos de dominio en lugar de primitivos

### Principios clave

-   Representar conceptos con tipos propios.
-   Evitar uso excesivo de `String`, `int`, etc.

------------------------------------------------------------------------

# 21. No usar códigos de error ambiguos

### Principios clave

-   No usar valores especiales como `null`, `-1` o `"ERROR"`.
-   Preferir excepciones u objetos expresivos.

------------------------------------------------------------------------

# 22. Código fácil de refactorizar

### Principios clave

-   Estructura flexible.
-   Bajo acoplamiento.
-   Facilitar cambios futuros.

------------------------------------------------------------------------

# 23. Minimizar conocimiento disperso

### Principios clave

-   Centralizar reglas importantes.
-   Evitar lógica fragmentada.

------------------------------------------------------------------------

# 24. Consistencia semántica

### Principios clave

-   Usar los mismos términos para los mismos conceptos.
-   Mantener coherencia en nombres y estructuras.

------------------------------------------------------------------------

# 25. Claridad sobre ingenio

### Principios clave

-   Evitar soluciones "inteligentes" difíciles de leer.
-   Priorizar claridad.

------------------------------------------------------------------------

# 26. Evitar sobrecompactación

### Principios clave

-   No sacrificar legibilidad por brevedad.
-   Expresar intención claramente.

------------------------------------------------------------------------

# 27. Código listo para leer

### Principios clave

-   El código debe entenderse sin explicación adicional.
-   Debe comunicar intención por sí mismo.

------------------------------------------------------------------------

# Conclusión

Estas reglas fortalecen el desarrollo de software más allá de la
arquitectura, enfocándose en:

-   legibilidad,
-   diseño de funciones,
-   cohesión,
-   expresividad,
-   y mantenibilidad.

Un sistema bien estructurado arquitectónicamente puede fallar si su
código interno no es claro.\
Por ello, estas prácticas son esenciales para alcanzar un nivel
profesional en el desarrollo de software.
