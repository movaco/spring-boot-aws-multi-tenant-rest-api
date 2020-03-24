package de.movaco.server.security.cognito;

import java.util.Collection;
import lombok.EqualsAndHashCode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@EqualsAndHashCode(callSuper = true)
public class CognitoAuthenticationToken extends AbstractAuthenticationToken {

  private final String tenant;
  private final CognitoUserPrincipal principal;
  private final String accessToken;

  CognitoAuthenticationToken(
      String userName,
      String accessToken,
      Collection<? extends GrantedAuthority> authorities,
      String tenant) {
    super(authorities);
    this.tenant = tenant;
    this.principal = new CognitoUserPrincipal(userName);
    this.accessToken = accessToken;
  }

  public String getTenant() {
    return this.tenant;
  }

  public String getUserName() {
    return principal.getUsername();
  }

  @Override
  public Object getCredentials() {
    return this.accessToken;
  }

  @Override
  public Object getPrincipal() {
    return this.principal;
  }
}
