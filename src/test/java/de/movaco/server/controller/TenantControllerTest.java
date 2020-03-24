package de.movaco.server.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.movaco.server.multi_tenancy.SecurityContextTenantResolver;
import de.movaco.server.multi_tenancy.TenantContext;
import de.movaco.server.persistence.entities.TenantEntity;
import de.movaco.server.persistence.repository.TenantRepository;
import de.movaco.server.service.DatabaseMigrationService;
import de.movaco.server.service.TenantService;
import de.movaco.server.shared.SuperAdminUser;
import de.movaco.server.shared.TestUser;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class TenantControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TenantRepository tenantRepository;

  @Autowired private TenantService tenantService;

  @Autowired private DatabaseMigrationService databaseMigrationService;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private SecurityContextTenantResolver securityContextTenantResolver;

  @Test
  @SuperAdminUser
  public void createTenant() throws Exception {
    this.mockMvc
        .perform(
            get(
                RestEndpoints.TENANTS
                    + RestEndpoints.TENANTS_CREATE
                    + "?tenantName=TestTenant&schemaName=newtesttenant"))
        .andExpect(status().isOk());
    assertThat(this.tenantService.getAllTenantNames()).contains("TestTenant");
  }

  @Test
  @TestUser(username = "tester")
  public void getTenantName() throws Exception {
    this.tenantRepository.deleteAll();
    this.tenantRepository.save(new TenantEntity("TestTenant", "Test Tenant"));
    this.databaseMigrationService.migrateTenantSchema("TestTenant");
    when(this.securityContextTenantResolver.resolveTenant())
        .thenReturn(java.util.Optional.of("TestTenant"));
    TenantContext.setCurrentTenant("TestTenant");
    String receivedName =
        this.mockMvc
            .perform(get(RestEndpoints.TENANTS + RestEndpoints.TENANTS_GET_NAME))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
    assertThat(receivedName).isEqualTo("Test Tenant");
  }

  @AfterEach
  private void clearTenant() {
    TenantContext.clear();
    this.tenantRepository.deleteAll();
  }

  @Test
  @SuperAdminUser
  public void deleteTenant() throws Exception {
    UUID tenantId =
        this.tenantRepository.save(new TenantEntity("deleteschema", "Some name")).getId();
    this.mockMvc
        .perform(get(RestEndpoints.TENANTS + RestEndpoints.TENANTS_DELETE + "/" + tenantId))
        .andExpect(status().isOk());
    assertThat(tenantRepository.findById(tenantId)).isNotPresent();
  }

  @Test
  @TestUser(username = "randomUser")
  public void createTenantShouldBeForbiddenIfUserIsNotAnAdmin() throws Exception {
    this.mockMvc
        .perform(
            get(
                RestEndpoints.TENANTS
                    + RestEndpoints.TENANTS_CREATE
                    + "?tenantName=TestTenant&schemaName=testtenant"))
        .andExpect(status().isForbidden());
  }
}
