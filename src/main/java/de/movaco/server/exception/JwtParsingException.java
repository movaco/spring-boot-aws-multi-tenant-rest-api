package de.movaco.server.exception;

public class JwtParsingException extends RuntimeException {

  public JwtParsingException(String message, Throwable cause) {
    super(message, cause);
  }

  public JwtParsingException(String s) {
    super(s);
  }
}
