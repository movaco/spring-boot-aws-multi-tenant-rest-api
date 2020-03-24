package de.movaco.server.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;

import de.movaco.server.persistence.entities.TenantEntity;
import de.movaco.server.persistence.specification.TenantSpecifications;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class TenantRepositoryTest {

  @Autowired private TenantRepository tenantRepository;

  @BeforeEach
  public void clearRepository() {
    this.tenantRepository.deleteAll();
  }

  @Test
  public void persistAndFindNewTenant() {
    TenantEntity testTenant = new TenantEntity("testTenant", "Test Tenant");
    TenantEntity savedTenant = this.tenantRepository.save(testTenant);
    assertThat(savedTenant).isEqualToComparingFieldByField(testTenant);
    assertThat(savedTenant.isTransient()).isFalse();
    assertThat(savedTenant.getVersion()).isGreaterThan(-1);
    TenantEntity loadedTenantEntity = this.tenantRepository.findById(testTenant.getId()).get();
    assertThat(loadedTenantEntity.getTenantName()).isEqualTo(testTenant.getTenantName());
    assertThat(loadedTenantEntity.getSchemaName()).isEqualTo(testTenant.getSchemaName());
  }

  @Test
  public void findByTenantName() {
    TenantEntity testTenant = new TenantEntity("testerTenant", "Test Tenant");
    this.tenantRepository.save(testTenant);
    UUID id =
        this.tenantRepository
            .findAll(TenantSpecifications.withTenantName(testTenant.getTenantName()))
            .get(0)
            .getId();
    assertThat(id).isEqualTo(testTenant.getId());
  }

  @Test
  public void findBySchemaName() {
    TenantEntity testTenant = new TenantEntity("testerTenant", "Test Tenant");
    this.tenantRepository.save(testTenant);
    UUID id =
        this.tenantRepository
            .findAll(TenantSpecifications.withSchemaName(testTenant.getSchemaName()))
            .get(0)
            .getId();
    assertThat(id).isEqualTo(testTenant.getId());
  }

  @Test
  public void findIdByTenantName() {
    TenantEntity testTenant = new TenantEntity("testerTenant", "Test Tenant");
    this.tenantRepository.save(testTenant);
    UUID id = tenantRepository.findIdByTenantName("Test Tenant");
    assertThat(id).isEqualTo(testTenant.getId());
  }
}
