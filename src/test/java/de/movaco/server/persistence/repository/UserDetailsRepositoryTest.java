package de.movaco.server.persistence.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import de.movaco.server.persistence.entities.UserDetailsEntity;
import de.movaco.server.shared.TestTenantContextBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserDetailsRepositoryTest extends TestTenantContextBase {

  private static final String TEST_USER_NAME = "tester";
  private static final UserDetailsEntity TEST_USER =
      new UserDetailsEntity(TEST_USER_NAME, "Helge", "Schneider", "test@user.de");

  @Autowired private UserDetailsRepository repository;

  @Test
  public void saveTestUserWorks() {
    UserDetailsEntity newEntity = TEST_USER;
    UserDetailsEntity savedEntity = this.repository.save(newEntity);
    assertUserEntitiesAreEquals(newEntity, savedEntity);
    UserDetailsEntity loadedEntity = this.repository.findById(savedEntity.getId()).get();
    assertUserEntitiesAreEquals(newEntity, loadedEntity);
    assertThat(savedEntity.isTransient()).isFalse();
    assertThat(savedEntity.getVersion()).isGreaterThan(-1);
  }

  private static void assertUserEntitiesAreEquals(
      UserDetailsEntity expected, UserDetailsEntity actual) {
    assertThat(actual)
        .isEqualToIgnoringGivenFields(
            expected, "settings", "id", "version", "timePersisted", "timeModified");
  }

  @Test
  public void throwsIfUsernameIsTooShort() {
    UserDetailsEntity tooShortName = TEST_USER;
    tooShortName.setUserName("a");
    assertThrows(RuntimeException.class, () -> repository.save(tooShortName));
  }

  @Test
  public void throwsIfEmailIsMalformed() {
    UserDetailsEntity wrongEmail = TEST_USER;
    wrongEmail.setEmail("dadada");
    assertThrows(RuntimeException.class, () -> repository.save(wrongEmail));
  }
}
