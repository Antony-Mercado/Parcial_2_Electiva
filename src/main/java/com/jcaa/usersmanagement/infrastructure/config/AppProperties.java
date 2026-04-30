package com.jcaa.usersmanagement.infrastructure.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppProperties {

  private static final String PROPERTIES_FILE = "application.properties";

  private final Properties properties;

  public AppProperties() {
    this(AppProperties.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE));
  }

  // Package-private — test entry point
  AppProperties(final InputStream stream) {
    this.properties = doLoad(stream);
  }

  private static Properties doLoad(final InputStream stream) {
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.requireNonNull() o Objects.isNull().
    // Para objetos siempre debe usarse Objects.isNull/nonNull, nunca operadores == o !=.
    if (stream == null) {
      throw new NullPointerException("File not found in classpath: " + PROPERTIES_FILE);
    }
    // VIOLACIÓN Regla 4: nombre abreviado "props" en lugar del nombre descriptivo "properties".
    // Los nombres deben ser claros y sin abreviaturas.
    final Properties props = new Properties();
    try (stream) {
      props.load(stream);
    } catch (final IOException exception) {
      throw ConfigurationException.becauseLoadFailed(exception);
    }
    return props;
  }

  public String get(final String key) {
    // VIOLACIÓN Regla 4: nombre abreviado "val" en lugar de "value".
    final String val = properties.getProperty(key);
    // VIOLACIÓN Regla 4: se usa == null en lugar de Objects.requireNonNull() o Objects.isNull().
    if (val == null) {
      throw new NullPointerException("Property not found in " + PROPERTIES_FILE + ": " + key);
    }
    return val;
  }

  public int getInt(final String key) {
    return Integer.parseInt(get(key));
  }
}
