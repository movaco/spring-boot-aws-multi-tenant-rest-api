package de.movaco.server.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@Entity(name = "tenant")
@Table(name = "tenant")
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class TenantEntity extends BaseEntity {

  @NotNull
  @Length(min = 3, max = 20)
  @Column(name = "schema_name")
  private String schemaName;

  @NotNull
  @Length(min = 3, max = 50)
  @Column(name = "tenant_name")
  private String tenantName;

  public TenantEntity(String schemaName, String tenantName) {
    this.schemaName = schemaName;
    this.tenantName = tenantName;
  }
}
