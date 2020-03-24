package de.movaco.server.security.cognito;

import de.movaco.server.multi_tenancy.SecurityContextTenantResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CognitoSecurityContextTenantResolver implements SecurityContextTenantResolver {

  @Override
  public Optional<String> resolveTenant() {
    return CognitoHelper.getTokenForCurrentSecurityContext()
        .map(CognitoAuthenticationToken::getTenant);
  }
}
