package com.jcaa.usersmanagement.infrastructure.entrypoint.desktop.cli.io;

import java.io.PrintStream;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ConsoleIO {

  private final Scanner scanner;
  private final PrintStream out;

  public String readRequired(final String prompt) {
    // VIOLACIÓN Regla 4: nombre abreviado "v" en lugar del nombre descriptivo "value".
    // Clean Code - Regla 24 (consistencia semántica):
    // El mismo concepto —"entrada del usuario leída de consola"— se llama "v" aquí
    // y "r" en readInt(), dentro de la misma clase. Nombres distintos para el mismo
    // concepto hacen que el lector asuma incorrectamente que son ideas diferentes.
    String v;
    do {
      out.print(prompt);
      v = scanner.nextLine().trim();
      if (v.isBlank()) {
        // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
        out.println("  Value cannot be blank. Please try again.");
      }
    } while (v.isBlank());
    return v;
  }

  public String readOptional(final String prompt) {
    out.print(prompt);
    return scanner.nextLine().trim();
  }

  public int readInt(final String prompt) {
    while (true) {
      out.print(prompt);
      // VIOLACIÓN Regla 4: nombre abreviado "r" en lugar del nombre descriptivo "rawInput".
      final String r = scanner.nextLine().trim();
      try {
        return Integer.parseInt(r);
      } catch (final NumberFormatException ignored) {
        // VIOLACIÓN Regla 10: texto hardcodeado directamente — debe ser una constante.
        out.println("  Invalid input. Please enter a number.");
      }
    }
  }

  public void println(final String message) { out.println(message); }
  public void println() { out.println(); }
  public void printf(final String format, final Object... args) { out.printf(format, args); }
}