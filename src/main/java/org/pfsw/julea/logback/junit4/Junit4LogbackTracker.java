// ===========================================================================
// CONTENT  : CLASS Junit4LogbackTracker
// AUTHOR   : Manfred Duchrow
// VERSION  : 1.0 - 26/08/2024
// HISTORY  :
//  26/08/2024  mdu  CREATED
//
// Copyright (c) 2024, by MDCS. All rights reserved.
// ===========================================================================
package org.pfsw.julea.logback.junit4;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.pfsw.julea.core.junit4.Junit4LogEntriesTracker;
import org.pfsw.julea.logback.BaseLogbackTracker;

public class Junit4LogbackTracker extends BaseLogbackTracker implements Junit4LogEntriesTracker
{
  /**
   * Tracks all loggers with the full qualified names of the given classes.
   * 
   * @param classes The class for which loggers with the full qualified class names are to be tracked.
   * @return A new tracker instance listening on the specified loggers. 
   */
  public static Junit4LogbackTracker track(Class<?>... classes)
  {
    return new Junit4LogbackTracker(classes);
  }

  /**
   * Tracks all loggers with given names.
   * 
   * @param loggerNames the names of loggers to be tracked.
   * @return A new tracker instance listening on the specified loggers. 
   */
  public static Junit4LogbackTracker track(String... loggerNames)
  {
    return new Junit4LogbackTracker(loggerNames);
  }

  protected Junit4LogbackTracker(Class<?>[] classes)
  {
    super(classes);
  }

  protected Junit4LogbackTracker(String[] loggerNames)
  {
    super(loggerNames);
  }

  @Override
  public Statement apply(Statement base, Description description)
  {
    return createStatement(base);
  }

  protected Statement createStatement(final Statement base)
  {
    return new Statement()
    {
      @Override
      public void evaluate() throws Throwable
      {
        initialization();
        try
        {
          base.evaluate();
        }
        finally
        {
          cleanup();
        }
      }
    };
  }

  @Override
  protected void initialization()
  {
    super.initialization();
  }

  @Override
  protected void cleanup()
  {
    super.cleanup();
  }
}
