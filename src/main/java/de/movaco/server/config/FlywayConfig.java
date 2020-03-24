package de.movaco.server.config;

import de.movaco.server.persistence.repository.TenantRepository;
import de.movaco.server.service.DatabaseMigrationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

  private static final Logger LOGGER = LogManager.getLogger();

  @Bean
  public Flyway flyway(DatabaseMigrationService databaseMigrationService) {
    LOGGER.info("Migrating default schema ");
    return databaseMigrationService.migrateGlobalTenantSchema();
  }

  @Bean
  public Boolean tenantsFlyway(
      TenantRepository repository, DatabaseMigrationService databaseMigrationService) {
    repository
        .findAll()
        .forEach(tenant -> databaseMigrationService.migrateTenantSchema(tenant.getSchemaName()));
    return true;
  }
}
