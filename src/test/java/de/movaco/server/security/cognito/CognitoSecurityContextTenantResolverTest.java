package de.movaco.server.security.cognito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class CognitoSecurityContextTenantResolverTest {

  @Test
  public void resolvesTenant() {
    SecurityContext context = mock(SecurityContext.class);
    CognitoAuthenticationToken authentication = mock(CognitoAuthenticationToken.class);
    when(authentication.getTenant()).thenReturn("testtenant");
    when(context.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(context);
    CognitoSecurityContextTenantResolver resolver = new CognitoSecurityContextTenantResolver();
    assertThat(resolver.resolveTenant().get()).isEqualTo("testtenant");
  }
}
