package de.movaco.server.security.cognito;

import com.nimbusds.jwt.JWTClaimsSet;
import de.movaco.server.exception.JwtParsingException;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CognitoAccessTokenVerifier {

  private final CognitoJWTProcessor jwtProcessor;

  @Value("${cognito.clientId}")
  private String clientId;

  @Value("${cognito.poolId}")
  private String poolId;

  @Value("${cognito.region}")
  private String region;

  private String issuer;

  @Autowired
  public CognitoAccessTokenVerifier(CognitoJWTProcessor jwtProcessor) {
    this.jwtProcessor = jwtProcessor;
  }

  @PostConstruct
  private void init() {
    this.issuer = "https://cognito-idp." + region + ".amazonaws.com/" + poolId;
  }

  public void verify(String jwt) {
    JWTClaimsSet claims = jwtProcessor.getClaims(jwt);
    if (claims.getExpirationTime().before(new Date())) {
      throw new JwtParsingException("Jwt is expired!");
    }
    if (!claims.getClaim("client_id").equals(clientId)) {
      throw new JwtParsingException("Client id does not match!");
    }
    if (!claims.getIssuer().equals(issuer)) {
      throw new JwtParsingException("Issuer does not match!");
    }
    if (!claims.getClaim("token_use").equals("access")) {
      throw new JwtParsingException("Token use must be access!");
    }
  }
}
