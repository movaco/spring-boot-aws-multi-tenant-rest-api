package de.movaco.server.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class SchemaService {

  private static final String SELECT_SCHEMAS_QUERY =
      "select schema_name from information_schema.schemata;";

  private final JdbcTemplate jdbcTemplate;

  @Autowired
  public SchemaService(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public Collection<String> listSchemas() {
    return this.jdbcTemplate.query(
        SELECT_SCHEMAS_QUERY, (rs, rowNum) -> rs.getString(1).toLowerCase());
  }

  public boolean schemaExists(String schemaName) {
    return listSchemas().contains(schemaName.toLowerCase());
  }
}
