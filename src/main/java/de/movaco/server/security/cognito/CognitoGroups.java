package de.movaco.server.security.cognito;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CognitoGroups {
  static final String COGNITO_ADMIN_GROUP_NAME = "admins";
  static final String COGNITO_SUPER_ADMIN_GROUP_NAME = "superAdmins";
}
