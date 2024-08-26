// ===========================================================================
// CONTENT  : CLASS BaseLogbackTracker
// AUTHOR   : Manfred Duchrow
// VERSION  : 1.0 - 26/08/2024
// HISTORY  :
//  26/08/2024  mdu  CREATED
//
// Copyright (c) 2024, by MDCS. All rights reserved.
// ===========================================================================
package org.pfsw.julea.logback;

import java.util.ArrayList;
import java.util.List;

import org.pfsw.julea.core.InMemoryLogAppender;
import org.pfsw.julea.core.InMemoryLogEntriesTracker;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * This log entry tracker is supposed to be extended and then used as unit test rule or registered extension.
 * It will collect log messages in memory and supports checking for existence or no-existence of specific log entries.
 * <p>
 * <code>
 * \@Rule public LogEntriesTracker logTracker = LogbackTracker.track(Service1.class, Handler3.class);
 * \@@RegisterExtension public LogEntriesTracker logTracker = LogbackTracker.track(Service1.class, Handler3.class);
 * </code>
 */
public abstract class BaseLogbackTracker implements InMemoryLogEntriesTracker
{
  private final List<Logger> loggers = new ArrayList<>();
  private LogbackInMemoryLogAppender logbackAppender;

  protected BaseLogbackTracker(Class<?>... classes)
  {
    for (Class<?> clazz : classes)
    {
      getLoggers().add((Logger)LoggerFactory.getLogger(clazz));
    }
  }

  protected BaseLogbackTracker(String... loggerNames)
  {
    for (String name : loggerNames)
    {
      getLoggers().add((Logger)LoggerFactory.getLogger(name));
    }
  }

  @Override
  public InMemoryLogAppender getAppender()
  {
    return getLogbackAppender();
  }

  @Override
  public String toString()
  {
    return String.format("%s()", getClass().getSimpleName());
  }

  protected void initialization()
  {
    Filter<ILoggingEvent> filter = new Filter<>()
    {
      @Override
      public FilterReply decide(ILoggingEvent event)
      {
        return FilterReply.ACCEPT;
      }
    };
    setLogbackAppender(LogbackInMemoryLogAppender.createAppender("in-memory", filter));
    getLogbackAppender().start();
    for (Logger logger : getLoggers())
    {
      logger.addAppender(getLogbackAppender());
    }
  }

  protected void cleanup()
  {
    clear();
    for (Logger logger : getLoggers())
    {
      logger.detachAppender(getLogbackAppender());
    }
    getLoggers().clear();
    getLogbackAppender().stop();
  }

  protected List<Logger> getLoggers()
  {
    return this.loggers;
  }

  protected LogbackInMemoryLogAppender getLogbackAppender()
  {
    return this.logbackAppender;
  }

  protected void setLogbackAppender(LogbackInMemoryLogAppender logbackAppender)
  {
    this.logbackAppender = logbackAppender;
  }
}
