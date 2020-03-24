package de.movaco.server.persistence.entities;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

  @Id private UUID id = UUID.randomUUID();

  @Version private Integer version;

  @Column(name = "time_persisted")
  private LocalDateTime timePersisted = LocalDateTime.now();

  @Column(name = "time_modified")
  private LocalDateTime timeModified = LocalDateTime.now();

  protected BaseEntity() {}

  protected BaseEntity(UUID id) {
    this.id = id;
  }

  @PreUpdate
  private void onUpdate() {
    this.timeModified = LocalDateTime.now();
  }

  @PrePersist
  private void onPersist() {
    this.timePersisted = LocalDateTime.now();
  }

  public boolean isTransient() {
    return this.version == null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BaseEntity that = (BaseEntity) o;
    return Objects.equals(this.id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.id);
  }
}
