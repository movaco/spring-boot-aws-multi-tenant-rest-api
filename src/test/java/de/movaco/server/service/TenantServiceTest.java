package de.movaco.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.movaco.server.exception.TenantAlreadyExistsException;
import de.movaco.server.exception.TenantNotFoundException;
import de.movaco.server.multi_tenancy.TenantContext;
import de.movaco.server.shared.SuperAdminUser;
import de.movaco.server.shared.TenantTestBase;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TenantServiceTest extends TenantTestBase {

  @Autowired private TenantService tenantService;

  @Test
  @SuperAdminUser
  public void createTenant() {
    TenantContext.clear();
    this.tenantService.createTenant("A new Tenant", "newTenant");
    assertThat(tenantService.getAllTenantNames()).contains("A new Tenant");
  }

  @Test
  @SuperAdminUser
  public void getTenantId() {
    TenantContext.clear();
    UUID id = this.tenantService.createTenant("Test Tenant Gmbh", "tTenant");
    UUID receivedId = this.tenantService.getTenantIdByTenantName("Test Tenant Gmbh");
    assertThat(id).isEqualTo(receivedId);
  }

  @Test
  public void getTenantIdThrowsIfTenantNotExist() {
    TenantContext.clear();
    assertThrows(
        TenantNotFoundException.class,
        () -> this.tenantService.getTenantIdByTenantName("Not existing Tenant"));
  }

  @Test
  @SuperAdminUser
  public void createTenantThrowsIfTenantAlreadyExist() {
    TenantContext.clear();
    assertThrows(
        TenantAlreadyExistsException.class,
        () -> this.tenantService.createTenant(TEST_TENANT_NAME, TEST_TENANT_SCHEMA_NAME));
  }

  @Test
  @SuperAdminUser
  public void createTenantThrowsIfSchemaAlreadyExist() {
    String schema = "supischema";
    TenantContext.clear();
    this.migrationService.migrateTenantSchema(schema);
    assertThrows(
        TenantAlreadyExistsException.class,
        () -> this.tenantService.createTenant(TEST_TENANT_NAME, schema));
  }

  @Test
  @SuperAdminUser
  public void deleteTenant() {
    TenantContext.clear();
    UUID id = this.tenantService.createTenant("Test Tenant Gmbh", "todelete");
    this.tenantService.deleteTenant(id);
    assertThat(this.tenantService.getAllTenantNames()).doesNotContain("Test Tenant Gmbh");
  }
}
