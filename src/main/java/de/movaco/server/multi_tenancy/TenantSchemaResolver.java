package de.movaco.server.multi_tenancy;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TenantSchemaResolver implements CurrentTenantIdentifierResolver {

  @Value("${db.tenantSchemaName}")
  private String tenantSchema;

  @Override
  public String resolveCurrentTenantIdentifier() {
    String tenant = TenantContext.getCurrentTenant();
    if (tenant == null) {
      return this.tenantSchema;
    }
    return tenant;
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
