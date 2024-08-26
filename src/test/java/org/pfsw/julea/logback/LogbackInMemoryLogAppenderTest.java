package org.pfsw.julea.logback;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LogbackInMemoryLogAppenderTest
{
  @Test
  public void test_toString()
  {
    assertEquals("LogbackInMemoryLogAppender()", LogbackInMemoryLogAppender.createAppender("test", null).toString());
  }
}
