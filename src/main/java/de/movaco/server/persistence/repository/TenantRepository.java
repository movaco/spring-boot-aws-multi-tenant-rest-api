package de.movaco.server.persistence.repository;

import de.movaco.server.persistence.entities.TenantEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;

public interface TenantRepository extends BaseRepository<TenantEntity> {

  @Query("select t.id from tenant t where t.tenantName = ?1 ")
  UUID findIdByTenantName(String tenantName);

  @Query("select t from tenant t where t.schemaName = ?1 ")
  List<TenantEntity> findByTenantSchemaName(String schemaName);
}
