package de.movaco.server.service;

import static de.movaco.server.service.AuthConstants.AUTHORIZED_USER;
import static de.movaco.server.service.AuthConstants.AUTHORIZED_USERNAME;

import de.movaco.server.exception.UserAlreadyExistsException;
import de.movaco.server.exception.UserDetailsNotFoundException;
import de.movaco.server.model.User;
import de.movaco.server.persistence.entities.UserDetailsEntity;
import de.movaco.server.persistence.repository.UserDetailsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserDetailsRepository userDetailsRepository;

  @Autowired
  public UserService(UserDetailsRepository userDetailsRepository) {
    this.userDetailsRepository = userDetailsRepository;
  }

  @PreAuthorize(AUTHORIZED_USER)
  public User createAppUser(User user) {
    throwIfUserAlreadyExists(user.getUserName());
    UserDetailsEntity userDetailsEntity = createUserDetailsEntity(user);
    return this.userDetailsRepository.save(userDetailsEntity);
  }

  @PreAuthorize(AUTHORIZED_USERNAME)
  public void deleteUser(String username) {
    this.userDetailsRepository.deleteById(getUserByUsername(username).getId());
  }

  @PreAuthorize(AUTHORIZED_USER)
  public User updateUserDetails(User user) {
    throwIfUserDoesNotExist(user.getUserName());
    UserDetailsEntity userEntity = getUserByUsername(user.getUserName());
    update(userEntity, user);
    return this.userDetailsRepository.save(userEntity);
  }

  @PreAuthorize(AUTHORIZED_USERNAME)
  public UserDetailsEntity getUserByUsername(String username) {
    List<UserDetailsEntity> users = this.userDetailsRepository.findByUserName(username);
    if (users.isEmpty()) {
      throw new UserDetailsNotFoundException(
          String.format("Could not find user details for username '%s'", username));
    }
    return users.get(0);
  }

  private UserDetailsEntity createUserDetailsEntity(User user) {
    return new UserDetailsEntity(
        user.getUserName(), user.getFirstName(), user.getLastName(), user.getEmail());
  }

  private void update(UserDetailsEntity entity, User update) {
    entity.setEmail(update.getEmail());
    entity.setFirstName(update.getFirstName());
    entity.setLastName(update.getLastName());
  }

  private void throwIfUserAlreadyExists(String userName) {
    if (userExists(userName)) {
      throw new UserAlreadyExistsException(
          String.format("User with username '%s' already exists!", userName));
    }
  }

  private void throwIfUserDoesNotExist(String userName) {
    if (!userExists(userName)) {
      throw new UserDetailsNotFoundException(
          String.format("User with username '%s' does not exists!", userName));
    }
  }

  private boolean userExists(String userName) {
    return !this.userDetailsRepository.findByUserName(userName).isEmpty();
  }
}
