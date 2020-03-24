package de.movaco.server.security.cognito;

import de.movaco.server.exception.NotAuthorizedException;
import de.movaco.server.security.AuthenticatedUserService;
import org.springframework.stereotype.Component;

@Component
public class CognitoAuthenticatedUserService implements AuthenticatedUserService {

  @Override
  public String getAuthenticatedUsername() {
    return CognitoHelper.getTokenForCurrentSecurityContext()
        .map(CognitoAuthenticationToken::getUserName)
        .orElseThrow(() -> new NotAuthorizedException("User not authenticated!"));
  }
}
