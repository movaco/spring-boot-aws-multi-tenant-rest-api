package de.movaco.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.HashMap;
import org.junit.jupiter.api.Test;

class JwtTokenUtilsTest {

  @Test
  void isTokenExpired() {
    String token =
        JwtTokenUtils.generateToken(
            new HashMap<>(), "mylittlesecret", LocalDateTime.now().plusHours(1));
    assertThat(JwtTokenUtils.isTokenExpired(token, "mylittlesecret")).isFalse();
    token =
        JwtTokenUtils.generateToken(
            new HashMap<>(), "mylittlesecret", LocalDateTime.now().minusHours(1));
    assertThat(JwtTokenUtils.isTokenExpired(token, "mylittlesecret")).isTrue();
  }

  @Test
  void getClaim() {
    String token =
        JwtTokenUtils.generateToken(
            new HashMap<String, Object>() {
              {
                put("name", "peter");
              }
            },
            "mylittlesecret",
            LocalDateTime.now().plusHours(1));
    String claim =
        JwtTokenUtils.getClaimFromToken(
            token, claims -> claims.get("name").toString(), "mylittlesecret");
    assertThat(claim).isEqualTo("peter");
    claim =
        JwtTokenUtils.getClaimFromToken(
            token,
            claims -> claims.containsKey("invalidkey") ? claims.get("invalidkey").toString() : "",
            "mylittlesecret");
    assertThat(claim).isEqualTo("");
  }
}
