package de.movaco.server.exception;

public class UserDetailsNotFoundException extends RuntimeException {

  public UserDetailsNotFoundException(String message) {
    super(message);
  }
}
