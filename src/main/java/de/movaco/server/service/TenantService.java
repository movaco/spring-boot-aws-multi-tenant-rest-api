package de.movaco.server.service;

import static de.movaco.server.security.Roles.ROLE_PREFIX;

import de.movaco.server.exception.TenantAlreadyExistsException;
import de.movaco.server.exception.TenantNotFoundException;
import de.movaco.server.multi_tenancy.SecurityContextTenantResolver;
import de.movaco.server.persistence.entities.TenantEntity;
import de.movaco.server.persistence.repository.TenantRepository;
import de.movaco.server.security.Roles;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

  private final SchemaService schemaService;
  private final TenantRepository repository;
  private final DatabaseMigrationService databaseMigrationService;
  private final SecurityContextTenantResolver securityContextTenantResolver;

  @Autowired
  public TenantService(
      SchemaService schemaService,
      TenantRepository repository,
      DatabaseMigrationService databaseMigrationService,
      SecurityContextTenantResolver securityContextTenantResolver) {
    this.schemaService = schemaService;
    this.repository = repository;
    this.databaseMigrationService = databaseMigrationService;
    this.securityContextTenantResolver = securityContextTenantResolver;
  }

  @Secured(ROLE_PREFIX + Roles.SUPER_ADMIN)
  public UUID createTenant(String tenantName, String schemaName) {
    if (this.schemaService.schemaExists(schemaName)) {
      throw new TenantAlreadyExistsException(
          String.format("Schema with name '%s' already exists!", schemaName));
    }
    if (repository.findIdByTenantName(tenantName) != null) {
      throw new TenantAlreadyExistsException(
          String.format("Tenant with name '%s' already exists!", tenantName));
    }
    TenantEntity tenantEntity = repository.save(new TenantEntity(schemaName, tenantName));
    this.databaseMigrationService.migrateTenantSchema(tenantEntity.getSchemaName());
    return tenantEntity.getId();
  }

  public List<String> getAllTenantNames() {
    return this.repository.findAll().stream()
        .map(TenantEntity::getTenantName)
        .collect(Collectors.toList());
  }

  UUID getTenantIdByTenantName(String tenantName) {
    UUID idForTenant = repository.findIdByTenantName(tenantName);
    if (idForTenant == null) {
      throw new TenantNotFoundException(
          String.format("Could not find tenant with name '%s'", tenantName));
    }
    return idForTenant;
  }

  @Secured(ROLE_PREFIX + Roles.SUPER_ADMIN)
  public void deleteTenant(UUID tenantId) {
    repository.deleteById(tenantId);
  }

  public String getTenantName() {
    String tenantSchema =
        this.securityContextTenantResolver
            .resolveTenant()
            .orElseThrow(
                () -> new TenantNotFoundException("Tenant not found! Cannot provide tenant name."));
    return this.repository.findByTenantSchemaName(tenantSchema).get(0).getTenantName();
  }
}
