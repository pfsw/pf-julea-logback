package org.pfsw.julea.logback.junit5;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class Junit5LogbackTrackerTest
{
  @Test
  public void test_toString()
  {
    assertEquals("Junit5LogbackTracker()", Junit5LogbackTracker.track(Junit5LogbackTrackerTest.class).toString());
  }
}
