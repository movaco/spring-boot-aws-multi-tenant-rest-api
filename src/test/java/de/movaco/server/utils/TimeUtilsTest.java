package de.movaco.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class TimeUtilsTest {

  @Test
  void secondsTo() {
    assertThat(TimeUtils.secondsTo(LocalTime.of(0, 5), LocalTime.of(0, 5))).isEqualTo(0);
    assertThat(TimeUtils.secondsTo(LocalTime.of(0, 5), LocalTime.of(0, 10))).isEqualTo(5 * 60);
    assertThat(TimeUtils.secondsTo(LocalTime.of(23, 55), LocalTime.of(0, 10))).isEqualTo(15 * 60);
  }

  @Test
  void secondsAfterMidnight() {
    assertThat(TimeUtils.secondsAfterMidnight(LocalTime.of(0, 5))).isEqualTo(5 * 60);
  }

  @Test
  void secondsToMidnight() {
    assertThat(TimeUtils.secondsToMidnight(LocalTime.of(23, 5))).isEqualTo(55 * 60);
  }
}
