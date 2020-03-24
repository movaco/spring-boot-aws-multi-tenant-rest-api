package de.movaco.server.security.cognito;

import java.io.Serializable;

public class CognitoUserPrincipal implements Serializable {

  private final String username;

  public CognitoUserPrincipal(String username) {
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
