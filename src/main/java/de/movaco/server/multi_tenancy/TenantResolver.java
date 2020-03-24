package de.movaco.server.multi_tenancy;

import de.movaco.server.controller.RestEndpoints;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TenantResolver {

  private final SecurityContextTenantResolver securityContextTenantResolver;

  @Value("${db.tenantSchemaName}")
  private String tenantSchema;

  @Autowired
  public TenantResolver(SecurityContextTenantResolver securityContextTenantResolver) {
    this.securityContextTenantResolver = securityContextTenantResolver;
  }

  public Optional<String> detectTenantSchemaName(HttpServletRequest request) {
    if (isDefaultSchemaRequest(request)) {
      return Optional.of(this.tenantSchema);
    } else {
      return this.securityContextTenantResolver.resolveTenant();
    }
  }

  private static boolean isDefaultSchemaRequest(HttpServletRequest request) {
    String uri = request.getRequestURI();
    return uri.contains(RestEndpoints.TENANTS);
  }
}
