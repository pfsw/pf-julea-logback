package org.pfsw.julea.logback.junit4;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Junit4LogbackTrackerTest
{
  @Test
  public void test_toString()
  {
    assertEquals("Junit4LogbackTracker()", Junit4LogbackTracker.track(Junit4LogbackTrackerTest.class).toString());
  }
}
