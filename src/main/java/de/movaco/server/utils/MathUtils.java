package de.movaco.server.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MathUtils {

  public static float roundTwoDecimals(float value) {
    return Math.round(10f * value) / 10f;
  }
}
