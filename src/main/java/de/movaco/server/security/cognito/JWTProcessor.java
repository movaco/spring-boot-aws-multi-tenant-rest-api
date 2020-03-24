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
import java.net.URL;
import java.text.ParseException;

class JWTProcessor extends DefaultJWTProcessor<SimpleSecurityContext> {

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
