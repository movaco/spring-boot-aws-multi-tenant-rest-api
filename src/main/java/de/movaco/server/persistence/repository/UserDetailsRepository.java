package de.movaco.server.persistence.repository;

import de.movaco.server.persistence.entities.UserDetailsEntity;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface UserDetailsRepository extends BaseRepository<UserDetailsEntity> {

  @Query("select u from user_details u where u.userName = ?1 ")
  List<UserDetailsEntity> findByUserName(String userName);
}
