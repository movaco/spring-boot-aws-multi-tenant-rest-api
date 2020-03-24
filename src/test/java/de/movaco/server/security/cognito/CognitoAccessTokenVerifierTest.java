package de.movaco.server.security.cognito;

import static de.movaco.server.security.cognito.TestHelper.USER_TOKEN;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nimbusds.jwt.JWTClaimsSet;
import de.movaco.server.exception.JwtParsingException;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

class CognitoAccessTokenVerifierTest {

  @Test
  public void throwsIfExpired() {
    assertThrows(
        JwtParsingException.class,
        () -> {
          JWTClaimsSet.Builder builder =
              new JWTClaimsSet.Builder().expirationTime(new DateTime().minusDays(1).toDate());
          CognitoAccessTokenVerifier verifier = forClaimSetBuild(builder);
          verifier.verify(USER_TOKEN);
        });
  }

  private static CognitoAccessTokenVerifier forClaimSetBuild(JWTClaimsSet.Builder builder) {
    CognitoJWTProcessor processor = mock(CognitoJWTProcessor.class);
    when(processor.getClaims(any())).thenReturn(builder.build());
    return new CognitoAccessTokenVerifier(processor);
  }
}
