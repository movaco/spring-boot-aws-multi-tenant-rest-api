package de.movaco.server.security.cognito;

import static de.movaco.server.security.cognito.TestHelper.ADMIN_TOKEN;
import static de.movaco.server.security.cognito.TestHelper.USER_TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import de.movaco.server.security.Roles;
import java.util.List;
import org.junit.jupiter.api.Test;

class CognitoAccessTokenReaderTest {

  @Test
  public void getUserRoles() {
    CognitoAccessTokenReader reader =
        new CognitoAccessTokenReader(mock(CognitoAccessTokenVerifier.class));
    List<String> roles = reader.getRoles(USER_TOKEN);
    assertThat(roles).containsOnly(Roles.USER);
  }

  @Test
  public void getAdminRoles() {
    CognitoAccessTokenReader reader =
        new CognitoAccessTokenReader(mock(CognitoAccessTokenVerifier.class));
    List<String> roles = reader.getRoles(ADMIN_TOKEN);
    assertThat(roles).containsOnly(Roles.USER, Roles.ADMIN);
  }

  @Test
  public void getUserName() {
    CognitoAccessTokenReader reader =
        new CognitoAccessTokenReader(mock(CognitoAccessTokenVerifier.class));
    String userName = reader.getUserName(USER_TOKEN);
    assertThat(userName).isEqualTo("unitTestUser");
  }
}
