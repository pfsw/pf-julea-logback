// ===========================================================================
// CONTENT  : CLASS Junit5LogbackAsserter
// AUTHOR   : Manfred Duchrow
// VERSION  : 1.0 - 26/08/2024
// HISTORY  :
//  26/08/2024  mdu  CREATED
//
// Copyright (c) 2024, by MDCS. All rights reserved.
// ===========================================================================
package org.pfsw.julea.logback.junit5;

import org.pfsw.julea.core.junit5.Junit5LogAsserter;

/**
 * This interface is supposed to be implemented by a JUnit 5 test class to
 * inherit the default implementation of assertion methods for log entries checking. 
 */
public interface Junit5LogbackAsserter extends Junit5LogAsserter<Junit5LogbackTracker>
{
  /**
   * Tracks all loggers with given names.
   * 
   * @param loggerNames the names of loggers to be tracked.
   * @return A new tracker instance listening on the specified loggers. 
   */
  @Override
  default Junit5LogbackTracker trackLogger(String... loggerNames)
  {
    return Junit5LogbackTracker.track(loggerNames);
  }
}
