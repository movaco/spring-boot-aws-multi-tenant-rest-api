package de.movaco.server.security.cognito;

import com.nimbusds.jwt.JWTClaimsSet;
import de.movaco.server.exception.JwtParsingException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CognitoJWTProcessor {

  @Value("${cognito.poolId}")
  private String poolId;

  @Value("${cognito.region}")
  private String region;

  private JWTProcessor jwtProcessor;

  @PostConstruct
  private void init() {
    this.jwtProcessor = new JWTProcessor(getJwkUrl());
  }

  JWTClaimsSet getClaims(String jwt) {
    return jwtProcessor.getClaims(jwt);
  }

  private URL getJwkUrl() {
    String url =
        "https://cognito-idp." + region + ".amazonaws.com/" + poolId + "/.well-known/jwks.json";
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new JwtParsingException("Could not build proper jwk URL object from " + url, e);
    }
  }
}
