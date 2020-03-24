package de.movaco.server.shared;

import de.movaco.server.multi_tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class TestTenantContextBase extends TenantTestBase {

  @BeforeEach
  public void setTestTenant() {
    TenantContext.setCurrentTenant(TEST_TENANT_SCHEMA_NAME);
  }

  @AfterEach
  public void releaseTestTenant() {
    TenantContext.clear();
  }
}
