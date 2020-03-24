package de.movaco.server.exception;

public class NoUserAccountException extends RuntimeException {

  public NoUserAccountException(String message, Throwable cause) {
    super(message, cause);
  }
}
