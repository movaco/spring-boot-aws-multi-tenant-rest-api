package de.movaco.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.movaco.server.dto.UserDTO;
import de.movaco.server.exception.UserAlreadyExistsException;
import de.movaco.server.exception.UserDetailsNotFoundException;
import de.movaco.server.model.User;
import de.movaco.server.persistence.entities.UserDetailsEntity;
import de.movaco.server.persistence.repository.UserDetailsRepository;
import de.movaco.server.shared.AdminUser;
import de.movaco.server.shared.TestTenantContextBase;
import de.movaco.server.shared.TestUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

class UserServiceTest extends TestTenantContextBase {

  private static final String TEST_USER_NAME = "tester";
  private static final UserDetailsEntity TEST_USER_ENTITY =
      new UserDetailsEntity(TEST_USER_NAME, "Helge", "Schneider", "test@user.de");
  private static final UserDTO TEST_USER = UserDTO.of(TEST_USER_ENTITY);

  @Autowired private UserService userService;
  @Autowired private UserDetailsRepository userDetailsRepository;

  @Test
  @TestUser
  public void createAndDeleteUser() {
    User user = this.userService.createAppUser(TEST_USER);
    assertUsersAreEquals(TEST_USER, this.userService.getUserByUsername(user.getUserName()));
    this.userService.deleteUser(TEST_USER_NAME);
    assertThrows(
        UserDetailsNotFoundException.class,
        () -> this.userService.getUserByUsername(TEST_USER_NAME));
  }

  @Test
  @TestUser
  public void updateUserWhenRequestedBySameUser() {
    updateUserWorks();
  }

  private void updateUserWorks() {
    this.userService.createAppUser(TEST_USER);
    UserDTO updatedUser =
        new UserDTO(TEST_USER.getUserName(), "New Firstname", "New Lastname", "new@email.com");
    this.userService.updateUserDetails(updatedUser);
    UserDetailsEntity user = this.userService.getUserByUsername(TEST_USER.getUserName());
    assertUsersAreEquals(updatedUser, user);
  }

  private void assertUsersAreEquals(UserDTO expected, UserDetailsEntity actual) {
    assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
    assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
    assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
  }

  @Test
  @AdminUser()
  public void shouldThrowIfUserAddedTwice() {
    this.userService.createAppUser(TEST_USER);
    assertThrows(UserAlreadyExistsException.class, () -> this.userService.createAppUser(TEST_USER));
  }

  @Test
  @AdminUser()
  public void shouldThrowIfUserRequestedDoesNotExistAddedTwice() {
    assertThrows(
        UserDetailsNotFoundException.class, () -> this.userService.updateUserDetails(TEST_USER));
  }

  @Test
  @AdminUser()
  public void updateUserWhenRequestedByAdmin() {
    updateUserWorks();
  }

  @Test
  @TestUser(username = "differentuser")
  public void updateUserFailsIfRequestedFromAnotherUser() {
    assertThrows(AccessDeniedException.class, () -> this.userService.updateUserDetails(TEST_USER));
  }

  @Test
  @TestUser(username = "differentuser")
  public void getUserFailsIfRequestedFromAnotherUser() {
    assertThrows(
        AccessDeniedException.class, () -> this.userService.getUserByUsername(TEST_USER_NAME));
  }

  @Test
  @AdminUser()
  public void getUserByUsernameThrowsIfUserDoesNotExist() {
    assertThrows(
        UserDetailsNotFoundException.class, () -> this.userService.getUserByUsername("gost"));
  }
}
