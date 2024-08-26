// ===========================================================================
// CONTENT  : CLASS LogbackInMemoryLogAppender
// AUTHOR   : Manfred Duchrow
// VERSION  : 1.0 - 26/08/2024
// HISTORY  :
//  26/08/2024  mdu  CREATED
//
// Copyright (c) 2024, by MDCS. All rights reserved.
// ===========================================================================
package org.pfsw.julea.logback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pfsw.julea.core.InMemoryLogAppender;
import org.pfsw.julea.core.InMemoryLogRecordsCollector;
import org.pfsw.julea.core.LogLevel;
import org.pfsw.julea.core.LogRecord;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.filter.Filter;

/**
 * This logback appender collects log messages in memory.
 */
public class LogbackInMemoryLogAppender extends AppenderBase<ILoggingEvent> implements InMemoryLogAppender
{
  private static final Map<Level, LogLevel> LOG_LEVEL_MAPPING = initLogLevelMapping();

  private final InMemoryLogRecordsCollector logRecordsCollector = InMemoryLogRecordsCollector.create();

  public static LogbackInMemoryLogAppender createAppender(String appenderName, Filter<ILoggingEvent> filter)
  {
    return new LogbackInMemoryLogAppender(appenderName, filter);
  }

  private static Map<Level, LogLevel> initLogLevelMapping()
  {
    Map<Level, LogLevel> mapping = new HashMap<>();

    mapping.put(Level.ALL, LogLevel.ALL);
    mapping.put(Level.TRACE, LogLevel.TRACE);
    mapping.put(Level.DEBUG, LogLevel.DEBUG);
    mapping.put(Level.INFO, LogLevel.INFO);
    mapping.put(Level.WARN, LogLevel.WARN);
    mapping.put(Level.ERROR, LogLevel.ERROR);
    // FATAL not supported by logback
    mapping.put(Level.OFF, LogLevel.OFF);

    return mapping;
  }

  protected LogbackInMemoryLogAppender(String appenderName, Filter<ILoggingEvent> filter)
  {
    super();
    setName(appenderName);
    addFilter(filter);
  }

  @Override
  public void append(ILoggingEvent event)
  {
    getLogRecordsCollector().append(LogRecord.create(mapLogLevel(event.getLevel()), event.getFormattedMessage()));
  }

  @Override
  public void clear()
  {
    getLogRecordsCollector().clear();
  }
  
  @Override
  public void start()
  {
    clear();
    super.start();
  }

  @Override
  public void stop()
  {
    clear();
    super.stop();
  }

  @Override
  public String toString()
  {
    return String.format("%s()", getClass().getSimpleName());
  }

  @Override
  public List<LogRecord> getLogRecords()
  {
    return getLogRecordsCollector().getLogRecords();
  }

  protected LogLevel mapLogLevel(Level level)
  {
    return LOG_LEVEL_MAPPING.getOrDefault(level, LogLevel.OFF);
  }

  protected InMemoryLogRecordsCollector getLogRecordsCollector()
  {
    return this.logRecordsCollector;
  }
}
