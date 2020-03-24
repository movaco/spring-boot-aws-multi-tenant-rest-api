package de.movaco.server.service;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationService {

  @Value("${db.tenantSchemaName}")
  private String tenantSchema;

  private final DataSource dataSource;

  private static final String DEFAULT_MIGRATION_FOLDER = "db/migration/default";
  private static final String TENANT_MIGRATION_FOLDER = "db/migration/tenants";

  public DatabaseMigrationService(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public Flyway migrateGlobalTenantSchema() {
    return migrate(this.dataSource, this.tenantSchema, DEFAULT_MIGRATION_FOLDER);
  }

  public void migrateTenantSchema(String tenantSchemaName) {
    migrate(this.dataSource, tenantSchemaName, TENANT_MIGRATION_FOLDER);
  }

  private static Flyway migrate(DataSource dataSource, String schema, String locations) {
    ClassicConfiguration classicConfiguration = new ClassicConfiguration();
    classicConfiguration.setDataSource(dataSource);
    classicConfiguration.setLocationsAsStrings(locations);
    classicConfiguration.setSchemas(schema);
    Flyway flyway = new Flyway(classicConfiguration);
    flyway.migrate();
    return flyway;
  }
}
