package com.nicehash.clients.domain;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Test;

public class CurrencyTest {

  @Test
  public void performance() {
    double t1 = valueOfPerformance();
    double t2 = speedValueOfPerformance();
    System.out.println("Improvement: " + (1 - t2 / t1) * 100 + "%");
  }

  public double valueOfPerformance() {
    EnumSet<Currency> set = EnumSet.allOf(Currency.class);
    List<String> names = set.stream().map(Enum::name).collect(Collectors.toList());
    final long endTime, startTime = System.nanoTime();
    for (int i = 0; i < 1000000; i++) {
      for (String n : names) {
        Currency a = Currency.valueOf(n);
      }
    }
    endTime = System.nanoTime();
    System.out.println("valueOf duration: " + (endTime - startTime) / 1000000000.0 + " s");
    return (endTime - startTime) / 1000000000.0;
  }

  public double speedValueOfPerformance() {
    EnumSet<Currency> set = EnumSet.allOf(Currency.class);
    List<String> names = set.stream().map(Enum::name).collect(Collectors.toList());
    final long endTime, startTime = System.nanoTime();
    for (int i = 0; i < 1000000; i++) {
      for (String n : names) {
        Currency a = Currency.valueOfOptimized(n);
      }
    }
    endTime = System.nanoTime();
    System.out.println("Speed valueOf Duration: " + (endTime - startTime) / 1000000000.0 + " s");
    return (endTime - startTime) / 1000000000.0;
  }

  @Test
  public void speedValueOfTest() throws Exception {
    EnumSet<Currency> set = EnumSet.allOf(Currency.class);
    List<String> names = set.stream().map(Enum::name).collect(Collectors.toList());
    for (String n : names) {
      Currency a = Currency.valueOfOptimized(n);
      Currency b = Currency.valueOf(n);
      Assert.assertEquals(b, a);
    }

    try {
      Currency b = Currency.valueOf("Neznan");
      throw new Exception("Tukaj ne sme biti");
    } catch (IllegalArgumentException e) {
      System.out.println(e);
    }

    try {
      Currency b = Currency.valueOfOptimized("Neznan");
      throw new Exception("Tukaj ne sme biti");
    } catch (IllegalArgumentException e) {
      System.out.println(e);
    }
  }

  @Test
  public void jsonTest() {}
}
