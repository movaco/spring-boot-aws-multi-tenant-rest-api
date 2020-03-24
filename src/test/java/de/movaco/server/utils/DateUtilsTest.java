package de.movaco.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateUtilsTest {

  @Test
  void formatAsGermanDate() {
    assertThat(DateUtils.formatAsGermanDate(LocalDate.of(2019, 7, 14)))
        .isEqualTo("Sonntag, 14. Juli 2019");
  }
}
