package com.salon.util;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public class EventLogger {
  private static final String START_TIME = "09:00:00";
  private static LocalTime lt1 = LocalTime.parse(START_TIME, DateTimeFormatter.ofPattern("HH:mm:ss"));


  public static void log(long when, String event) {
    LocalTime lt2 = lt1.plusSeconds(when);
    System.out.println("[" + lt2.format(DateTimeFormatter.ofPattern("HH:mm")) + "] " + event);
  }
}
