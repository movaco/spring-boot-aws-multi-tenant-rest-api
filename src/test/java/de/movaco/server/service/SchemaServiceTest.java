package de.movaco.server.service;

import de.movaco.server.shared.TenantTestBase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class SchemaServiceTest extends TenantTestBase {

  @Test
  public void listSchemas() {
    Assertions.assertThat(this.schemaService.listSchemas()).contains("test", "test_tenants");
  }

  @Test
  public void schemaExists() {
    Assertions.assertThat(this.schemaService.schemaExists("test")).isTrue();
    Assertions.assertThat(this.schemaService.schemaExists("lohnischda")).isFalse();
  }
}
