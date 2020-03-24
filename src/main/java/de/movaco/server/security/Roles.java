package de.movaco.server.security;

public final class Roles {

  public static final String ROLE_PREFIX = "ROLE_";

  public static final String SUPER_ADMIN = "SUPER_ADMIN";
  public static final String ADMIN = "ADMIN";
  public static final String USER = "USER";

  public static String withRolePrefix(String role) {
    return ROLE_PREFIX + role;
  }

  private Roles() {
    // constants only
  }
}
