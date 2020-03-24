package de.movaco.server.shared;

import static org.mockito.Mockito.when;

import de.movaco.server.multi_tenancy.SecurityContextTenantResolver;
import de.movaco.server.multi_tenancy.TenantContext;
import de.movaco.server.persistence.entities.TenantEntity;
import de.movaco.server.persistence.repository.TenantRepository;
import de.movaco.server.persistence.repository.UserDetailsRepository;
import de.movaco.server.service.DatabaseMigrationService;
import de.movaco.server.service.SchemaService;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public abstract class TenantTestBase {

  protected static final String TEST_TENANT_SCHEMA_NAME = "test";
  protected static final String TEST_TENANT_NAME = "TEST";

  @Autowired private TenantRepository tenantRepository;
  @Autowired protected DatabaseMigrationService migrationService;
  @Autowired private UserDetailsRepository userDetailsRepository;
  @Autowired protected SchemaService schemaService;
  @MockBean private SecurityContextTenantResolver securityContextTenantResolver;

  @BeforeEach
  public void deleteAllTenantsAndCreateTestTenant() {
    TenantContext.clear();
    this.tenantRepository.deleteAll();
    this.migrationService.migrateTenantSchema(TEST_TENANT_SCHEMA_NAME);
    this.tenantRepository.save(new TenantEntity(TEST_TENANT_SCHEMA_NAME, TEST_TENANT_NAME));
    TenantContext.setCurrentTenant(TEST_TENANT_SCHEMA_NAME);
    when(securityContextTenantResolver.resolveTenant())
        .thenReturn(Optional.of(TEST_TENANT_SCHEMA_NAME));
  }

  @AfterEach
  public void cleanUp() {
    clearDB();
  }

  private void clearDB() {
    TenantContext.setCurrentTenant(TEST_TENANT_SCHEMA_NAME);
    this.userDetailsRepository.deleteAll();
    TenantContext.clear();
    this.tenantRepository.deleteAll();
  }
}
