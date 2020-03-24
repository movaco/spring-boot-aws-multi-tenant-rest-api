package de.movaco.server.security.cognito;

import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;

final class CognitoHelper {

  private CognitoHelper() {
    // utilities
  }

  static Optional<CognitoAuthenticationToken> getTokenForCurrentSecurityContext() {
    if (SecurityContextHolder.getContext() != null
        && SecurityContextHolder.getContext().getAuthentication() != null
        && SecurityContextHolder.getContext().getAuthentication()
            instanceof CognitoAuthenticationToken) {
      return Optional.of(
          (CognitoAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());
    }
    return Optional.empty();
  }
}
