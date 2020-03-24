package de.movaco.server.exception;

public class TenantAlreadyExistsException extends RuntimeException {

  public TenantAlreadyExistsException(String message) {
    super(message);
  }
}
