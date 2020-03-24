package de.movaco.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MathUtilsTest {

  @Test
  void roundTwoDecimalsWithTailingZero() {
    assertThat(MathUtils.roundTwoDecimals(1.234f)).isEqualTo(1.2f);
    assertThat(MathUtils.roundTwoDecimals(1.2f)).isEqualTo(1.2f);
    assertThat(MathUtils.roundTwoDecimals(1f)).isEqualTo(1f);
    assertThat(MathUtils.roundTwoDecimals(0.456f)).isEqualTo(0.5f);
  }
}
