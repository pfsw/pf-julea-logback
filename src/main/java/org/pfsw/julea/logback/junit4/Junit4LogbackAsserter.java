// ===========================================================================
// CONTENT  : CLASS Junit4LogbackAsserter
// AUTHOR   : Manfred Duchrow
// VERSION  : 1.0 - 26/08/2024
// HISTORY  :
//  26/08/2024  mdu  CREATED
//
// Copyright (c) 2024, by MDCS. All rights reserved.
// ===========================================================================
package org.pfsw.julea.logback.junit4;

import org.pfsw.julea.core.junit4.Junit4LogAsserter;

/**
 * This interface is supposed to be implemented by a JUnit 4 Test class to
 * inherit the default implementation of assertion methods for log entries checking. 
 */
public interface Junit4LogbackAsserter extends Junit4LogAsserter<Junit4LogbackTracker>
{
  /**
   * Tracks all loggers with given names.
   * 
   * @param loggerNames the names of loggers to be tracked.
   * @return A new tracker instance listening on the specified loggers. 
   */
  @Override
  default Junit4LogbackTracker trackLogger(String... loggerNames)
  {
    return Junit4LogbackTracker.track(loggerNames);
  }
}
