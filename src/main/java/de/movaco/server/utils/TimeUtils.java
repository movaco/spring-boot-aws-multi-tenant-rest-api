package de.movaco.server.utils;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public final class TimeUtils {

  private static final int SECONDS_PER_DAY = 24 * 60 * 60;

  private TimeUtils() {
    // utilities
  }

  public static int secondsTo(LocalTime from, LocalTime to) {
    if (from.equals(to)) {
      return 0;
    } else if (from.isBefore(to)) {
      return (int) ChronoUnit.SECONDS.between(from, to);
    } else {
      return (SECONDS_PER_DAY - (int) ChronoUnit.SECONDS.between(to, from));
    }
  }

  public static int secondsAfterMidnight(LocalTime time) {
    return secondsTo(LocalTime.MIDNIGHT, time);
  }

  public static int secondsToMidnight(LocalTime time) {
    return secondsTo(time, LocalTime.MIDNIGHT);
  }
}
