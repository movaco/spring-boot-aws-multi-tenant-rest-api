package de.movaco.server.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtTokenUtils {

  private static Date getExpirationDateFromToken(String token, String secret) {
    return getClaimFromToken(token, Claims::getExpiration, secret);
  }

  public static <T> T getClaimFromToken(
      String token, Function<Claims, T> claimsResolver, String secret) {
    final Claims claims = getAllClaimsFromToken(token, secret);
    return claimsResolver.apply(claims);
  }

  private static Claims getAllClaimsFromToken(String token, String secret) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
  }

  public static boolean isTokenExpired(String token, String secret) {
    try {
      final Date expiration = getExpirationDateFromToken(token, secret);
      return expiration.before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public static String generateToken(
      Map<String, Object> claims, String secret, LocalDateTime validUntil) {
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(Timestamp.valueOf(validUntil))
        .signWith(SignatureAlgorithm.HS512, secret)
        .compact();
  }
}
