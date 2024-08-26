// ===========================================================================
// CONTENT  : CLASS Junit5LogbackTracker
// AUTHOR   : Manfred Duchrow
// VERSION  : 1.0 - 26/08/2024
// HISTORY  :
//  26/08/2024  mdu  CREATED
//
// Copyright (c) 2024, by MDCS. All rights reserved.
// ===========================================================================
package org.pfsw.julea.logback.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.pfsw.julea.core.junit5.Junit5LogEntriesTracker;
import org.pfsw.julea.logback.BaseLogbackTracker;

public class Junit5LogbackTracker extends BaseLogbackTracker implements Junit5LogEntriesTracker
{
  /**
   * Tracks all loggers with the full qualified names of the given classes.
   * 
   * @param classes The class for which loggers with the full qualified class names are to be tracked.
   * @return A new tracker instance listening on the specified loggers. 
   */
  public static Junit5LogbackTracker track(Class<?>... classes)
  {
    return new Junit5LogbackTracker(classes);
  }

  /**
   * Tracks all loggers with given names.
   * 
   * @param loggerNames the names of loggers to be tracked.
   * @return A new tracker instance listening on the specified loggers. 
   */
  public static Junit5LogbackTracker track(String... loggerNames)
  {
    return new Junit5LogbackTracker(loggerNames);
  }

  protected Junit5LogbackTracker(Class<?>[] classes)
  {
    super(classes);
  }

  protected Junit5LogbackTracker(String[] loggerNames)
  {
    super(loggerNames);
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception
  {
    initialization();
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception
  {
    cleanup();
  }
}
