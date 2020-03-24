package de.movaco.server.service;

import de.movaco.server.security.Roles;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstants {

  public static final String IS_ADMIN = " hasRole('ROLE_" + Roles.ADMIN + "')";
  public static final String OR_IS_ADMIN = " or " + IS_ADMIN;
  public static final String AUTHORIZED_USER =
      "#user.userName == authentication.principal.username" + OR_IS_ADMIN;
  public static final String AUTHORIZED_USERNAME =
      "#username == authentication.principal.username" + OR_IS_ADMIN;
}
