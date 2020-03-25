package de.movaco.server.security.cognito;

import static com.nimbusds.jose.JWSAlgorithm.RS256;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.jwk.source.RemoteJWKSet;
import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.JWSVerificationKeySelector;
import com.nimbusds.jose.proc.SimpleSecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;
import de.movaco.server.exception.JwtParsingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
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

  private class JWTProcessor extends DefaultJWTProcessor<SimpleSecurityContext> {

    private final SimpleSecurityContext securityContext;

    private JWTProcessor(JWKSource<SimpleSecurityContext> keySource) {
      this.securityContext = new SimpleSecurityContext();
      JWSKeySelector<SimpleSecurityContext> keySelector =
          new JWSVerificationKeySelector<>(RS256, keySource);
      super.setJWSKeySelector(keySelector);
    }

    JWTProcessor(URL jwkUrl) {
      this(new RemoteJWKSet<>(jwkUrl, null));
    }

    JWTClaimsSet getClaims(String jwt) {
      try {
        return this.process(jwt, this.securityContext);
      } catch (ParseException | BadJOSEException | JOSEException e) {
        throw new JwtParsingException("Could not parse " + jwt, e);
      }
    }
  }
}
