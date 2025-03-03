package org.pfsw.julea.logback.junit5;

import static org.pfsw.julea.core.assertions.Junit5LogAssertions.assertLogEntry;
import static org.pfsw.julea.core.assertions.Junit5LogAssertions.assertLogEntryMessage;
import static org.pfsw.julea.core.assertions.Junit5LogAssertions.assertNoLogEntry;
import static org.pfsw.julea.core.assertions.Junit5LogAssertions.assertNoLogEntryMessage;
import static org.pfsw.julea.logback.testhelper.UnitTestHelper.MESSAGE_1;
import static org.pfsw.julea.logback.testhelper.UnitTestHelper.MESSAGE_2;
import static org.pfsw.julea.logback.testhelper.UnitTestHelper.STATS_LOGGER_NAME;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.pfsw.julea.core.LogLevel;
import org.pfsw.julea.core.junit5.Junit5LogEntriesTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit5Test
{
  private static final Logger LOG = LoggerFactory.getLogger(Junit5Test.class);
  private static final Logger STATS_LOG = LoggerFactory.getLogger(STATS_LOGGER_NAME);

  @RegisterExtension
  public Junit5LogEntriesTracker logTracker1 = Junit5LogbackTracker.track(Junit5Test.class);

  @RegisterExtension
  public Junit5LogEntriesTracker logTracker2 = Junit5LogbackTracker.track(STATS_LOGGER_NAME);

  @Test
  public void test_existing_log_entry_by_text_snippets()
  {
    LOG.info(MESSAGE_1);
    LOG.info(MESSAGE_2);
    assertLogEntry(logTracker1, "quick", "fox", "dog");
    assertLogEntry(logTracker1, "get");
  }

  @Test
  public void test_existing_log_entry_by_log_level_and_text_snippets()
  {
    LOG.info(MESSAGE_1);
    LOG.info(MESSAGE_2);
    assertLogEntry(logTracker1, LogLevel.INFO, "brown", "lazy");
    assertLogEntry(logTracker1, LogLevel.INFO, "You", "want", "always");
  }

  @Test
  public void test_existing_log_entry_by_regex_pattern()
  {
    LOG.error(MESSAGE_1);
    STATS_LOG.info(MESSAGE_2);
    assertLogEntryMessage(logTracker1, ".*The .*jumps.*");
    assertLogEntryMessage(logTracker2, ".*what you.*");
  }

  @Test
  public void test_not_existing_log_entry_by_text_snippets()
  {
    LOG.warn(MESSAGE_1);
    LOG.info(MESSAGE_2);
    assertNoLogEntry(logTracker1, "brown", "green");
    assertNoLogEntry(logTracker1, LogLevel.INFO, "brown", "fox");
    assertNoLogEntry(logTracker2, "brown");
  }

  @Test
  public void test_not_existing_log_entry_by_regex_pattern()
  {
    LOG.error(MESSAGE_1);
    LOG.warn(MESSAGE_2);
    assertNoLogEntryMessage(logTracker1, "^The .*over.*cat$");
  }
}
