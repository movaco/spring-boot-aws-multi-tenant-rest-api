package de.movaco.server.multi_tenancy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import de.movaco.server.controller.RestEndpoints;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class TenantResolverTest {

  @MockBean private SecurityContextTenantResolver securityContextTenantResolver;

  @Autowired private TenantResolver resolver;

  @Test
  void resolveDefaultSchemaName() {
    HttpServletRequest servletRequest = mock(HttpServletRequest.class);

    when(servletRequest.getRequestURI()).thenReturn(RestEndpoints.TENANTS);
    assertThat(this.resolver.detectTenantSchemaName(servletRequest).get())
        .isEqualTo("TEST_TENANTS");
  }

  @Test
  void resolveTenantSchemaName() {
    when(this.securityContextTenantResolver.resolveTenant()).thenReturn(Optional.of("test-realm"));
    HttpServletRequest servletRequest = mock(HttpServletRequest.class);

    when(servletRequest.getRequestURI()).thenReturn("/");
    assertThat(this.resolver.detectTenantSchemaName(servletRequest).get()).isEqualTo("test-realm");
  }
}
