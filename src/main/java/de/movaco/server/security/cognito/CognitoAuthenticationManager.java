package de.movaco.server.security.cognito;

import static de.movaco.server.security.Roles.withRolePrefix;

import de.movaco.server.security.Roles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class CognitoAuthenticationManager implements AuthenticationManager {

  private final CognitoAccessTokenReader accessTokenReader;
  private final CognitoTenantResolver tenantResolver;

  @Autowired
  public CognitoAuthenticationManager(
      CognitoAccessTokenReader accessTokenReader, CognitoTenantResolver tenantResolver) {
    this.accessTokenReader = accessTokenReader;
    this.tenantResolver = tenantResolver;
  }

  @Override
  public Authentication authenticate(Authentication authentication) {
    if (authentication instanceof CognitoAuthenticationToken) {
      return authentication;
    }
    String accessToken = (String) authentication.getPrincipal();
    List<String> roles = this.accessTokenReader.getRoles(accessToken);
    List<GrantedAuthority> authorities =
        roles.stream()
            .map(role -> new SimpleGrantedAuthority(withRolePrefix(role)))
            .collect(Collectors.toList());
    String userName = this.accessTokenReader.getUserName(accessToken);
    String tenant;
    if (roles.contains(Roles.SUPER_ADMIN)) {
      tenant = null;
    } else {
      tenant = this.tenantResolver.getTenant(userName);
    }
    return new CognitoAuthenticationToken(userName, accessToken, authorities, tenant);
  }
}
