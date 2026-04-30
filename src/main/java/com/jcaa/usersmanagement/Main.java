package com.jcaa.usersmanagement;

import com.jcaa.usersmanagement.infrastructure.config.DependencyContainer;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.UserManagementCli;
import com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io.ConsoleIO;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Clean Code - Regla 24 (consistencia semántica):
// Todo el proyecto usa java.util.logging.Logger (vía @Log de Lombok o Logger.getLogger()),
// pero esta clase usa org.slf4j.Logger + LoggerFactory de una librería diferente.
// El mismo concepto —"logger de la aplicación"— se resuelve con dos frameworks distintos
// sin justificación. Un lector no puede saber cuál es el estándar del proyecto.
// La regla dice: las mismas ideas deben resolverse igual en todo el proyecto.
//
// Clean Code - Regla 22 (código difícil de borrar y refactorizar):
// main() está acoplado directamente a tres clases concretas: DependencyContainer,
// UserManagementCli y ConsoleIO. Si se quiere reemplazar cualquiera de ellas
// (p. ej., cambiar el entrypoint de CLI a GUI), hay que editar el punto de entrada
// de la aplicación. No hay ninguna abstracción que proteja este acoplamiento.
public final class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  // Clean Code - Regla 1 (una sola cosa por función):
  // main() hace demasiadas cosas en un solo método:
  //   1. Construye el contenedor de dependencias (wiring completo de la app).
  //   2. Crea la infraestructura de I/O (Scanner + ConsoleIO).
  //   3. Instancia el CLI.
  //   4. Arranca el loop de ejecución.
  // Cada una de estas responsabilidades podría extraerse a un método con nombre claro:
  //   buildContainer(), buildConsole(), buildCli(), run().
  public static void main(final String[] args) {
    log.info("Starting Users Management System...");
    final DependencyContainer container = new DependencyContainer();
    try (final Scanner scanner = new Scanner(System.in)) {
      new UserManagementCli(container.userController(), new ConsoleIO(scanner, System.out)).start();
    }
  }
}