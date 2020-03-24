package de.movaco.server.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.Transient;
import javax.sql.DataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TenantConnectionProvider
    implements org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider {

  private static final Logger LOGGER = LogManager.getLogger();

  @Value("${db.tenantSchemaName}")
  private String tenantSchema;

  private static final long serialVersionUID = 1348353870772468815L;
  @Transient private final DataSource datasource;

  @Autowired
  public TenantConnectionProvider(DataSource dataSource) {
    this.datasource = dataSource;
  }

  @Override
  public Connection getAnyConnection() throws SQLException {
    return datasource.getConnection();
  }

  @Override
  public void releaseAnyConnection(Connection connection) throws SQLException {
    connection.close();
  }

  @Override
  public Connection getConnection(String tenantIdentifier) throws SQLException {
    LOGGER.debug("Get connection for tenant {}", tenantIdentifier);
    final Connection connection = getAnyConnection();
    connection.setSchema(tenantIdentifier);
    return connection;
  }

  @Override
  public void releaseConnection(String tenantIdentifier, Connection connection)
      throws SQLException {
    LOGGER.debug("Release connection for schema {}", tenantIdentifier);
    connection.setSchema(this.tenantSchema);
    releaseAnyConnection(connection);
  }

  @Override
  public boolean supportsAggressiveRelease() {
    return false;
  }

  @SuppressWarnings("rawtypes")
  @Override
  public boolean isUnwrappableAs(Class unwrapType) {
    return false;
  }

  @Override
  public <T> T unwrap(Class<T> unwrapType) {
    return null;
  }
}
