package de.movaco.server.persistence.repository;

import de.movaco.server.persistence.entities.BaseEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity>
    extends JpaRepository<T, UUID>, JpaSpecificationExecutor<T> {}
