package de.movaco.server.persistence.specification;

import de.movaco.server.persistence.entities.TenantEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TenantSpecifications {

  public static Specification<TenantEntity> withSchemaName(String schemaName) {
    return (tenant, cq, cb) -> cb.equal(tenant.get("schemaName"), schemaName);
  }

  public static Specification<TenantEntity> withTenantName(String tenantName) {
    return (tenant, cq, cb) -> cb.equal(tenant.get("tenantName"), tenantName);
  }
}
