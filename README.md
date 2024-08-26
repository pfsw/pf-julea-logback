# JUnit Log Event Assertion for logback (JULEA-logback)

The purpose of this library is to provide a simple way of tracking log events during the execution of JUnit tests
and asserting the existence or non-existence of specific log events during the test execution.

This is usually helpful if functionality gets tested by a unit test that does not return any result that could be directly asserted.
However, if there is some logging in the tested code it might be an option to ensure that specific log messages have been created
or have **not** been created during the execution of that code.

This library is built with JDK **8** and supports tracking of **logback** loggers only!  
It can be used with **JUnit4** and **JUnit5**.    

Of course this library can also be used with **jboss-logging** and **commons-logging** API as binding libraries to **slf4j** are
in the classpath.

Set the following maven or gradle dependencies as appropriate for your project:

## Dependencies

See also [mvnrepository](https://mvnrepository.com/artifact/org.pfsw/pf-julea-logback).

### JUnit 5

Maven:

````xml
<dependencies>
  <dependency>
    <groupId>org.pfsw</groupId>
    <artifactId>pf-julea-logback</artifactId>
    <version>1.0.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.5.7</version>
  </dependency>
  <dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.9.3</version>
    <scope>test</scope>
  </dependency>
</dependencies>
````

Gradle:

````groovy
dependencies {
  testImplementation group: 'org.pfsw', name: 'pf-julea-logback', version: '1.1.0'
  testImplementation group: 'org.apache.logging.log4j', name: 'log4j-core', version: '2.20.0'
  testImplementation group: 'org.junit.jupiter', name: 'junit-jupiter-api', version: '5.9.3'
}
````

### JUnit 4

Maven:

````xml
<dependencies>
  <dependency>
    <groupId>org.pfsw</groupId>
    <artifactId>pf-julea-logback</artifactId>
    <version>1.1.0</version>
    <scope>test</scope>
  </dependency>
  <dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-core</artifactId>
    <version>1.5.7</version>
  </dependency>
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.13.2</version>
    <scope>test</scope>
  </dependency>
</dependencies>
````

Gradle:

````groovy
dependencies {
  testImplementation group: 'org.pfsw', name: 'pf-julea-logback', version: '1.0.0'
  testImplementation group: 'ch.qos.logback', name: 'logback-classic', version: '1.5.7'
  testImplementation group: 'junit', name: 'junit', version: '4.13.2'
}
````

## Usage

Have a look at the following examples to learn how to use the library in your unit tests.  
JUnit4 and JUnit5 cannot be mixed in a single test class!

### JUnit5

**Variant 1**: Extending interface ``Junit5logbackAsserter``

- Implement interface ``Junit5logbackAsserter``
- Declare a field *logTracker* annotated as ``@RegisterExtension``
- Implement method ``public LogEntriesTracker getLogTracker()``

````java
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.pfsw.julea.core.LogEntriesTracker;
import org.pfsw.julea.logback.junit5.Junit5logbackAsserter;
import org.pfsw.julea.logback.junit5.Junit5LogEntriesTracker;
import org.pfsw.julea.logback.testhelper.LogId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit5AsserterTest implements Junit5logbackAsserter
{
  private static final Logger LOG = LoggerFactory.getLogger(Junit5AsserterTest.class);
  private enum LogId { ID001, ID002, ID003 }

  @RegisterExtension
  public Junit5LogEntriesTracker logTracker = trackLogger(Junit5AsserterTest.class);

  @Override
  public LogEntriesTracker getLogTracker()
  {
    return logTracker;
  }

  @Test
  public void test_log_entries()
  {
    LOG.info(String.format("[%s] The quick brown fox jumps over the lazy dog", LogId.ID001));
    LOG.warn(String.format("[%s] You can't always get what you want", LogId.ID002));
    assertLogEntry("quick", "fox", "dog");
    assertLogEntry("get");
    assertLogEntry(LogId.ID001);
    assertNoLogEntry(LogId.ID002, "fox");
    assertNoLogEntry(LogId.ID003);
  }
}
````

**Variant 2**: Static import of ``Junit5LogAssertions``

- Declare ``import static org.pfsw.julea.core.assertions.Junit5LogAssertions.*;``
- Declare a field *logTracker* annotated as ``@RegisterExtension`` using ``Junit5logbackTracker`` to initialize

````java
import static org.pfsw.julea.core.assertions.Junit5LogAssertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.pfsw.julea.core.LogLevel;
import org.pfsw.julea.logback.junit5.Junit5logbackTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit5Test
{
  private static final Logger LOG = LoggerFactory.getLogger(Junit5Test.class);

  @RegisterExtension
  public Junit5logbackTracker logTracker = Junit5logbackTracker.track(Junit5Test.class);

  @Test
  public void test_log_entries()
  {
    LOG.info("The quick brown fox jumps over the lazy dog");
    assertLogEntry(logTracker, "quick", "fox", "dog");
    assertLogEntryMessage(logTracker, "^The.*jumps over.*"); // regex
    assertNoLogEntry(logTracker, "cat");
  }
}
````

### JUnit4

**Variant 1**: Extending interface ``Junit4logbackAsserter``

- Implement interface ``Junit4logbackAsserter``
- Declare a field *logTracker* annotated as ``@Rule``
- Implement method ``public LogEntriesTracker getLogTracker()``


````java
import org.junit.Rule;
import org.junit.Test;
import org.pfsw.julea.core.LogEntriesTracker;
import org.pfsw.julea.logback.junit4.junit4.Junit4logbackAsserter;
import org.pfsw.julea.logback.junit4.junit4.Junit4LogEntriesTracker;
import org.pfsw.julea.logback.testhelper.LogId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit4AsserterTest implements Junit4logbackAsserter
{
  private static final Logger LOG = LoggerFactory.getLogger(Junit4AsserterTest.class);
  private enum LogId { ID001, ID002, ID003 }

  @Rule
  public Junit4LogEntriesTracker logTracker = trackLogger(Junit4AsserterTest.class);

  @Override
  public LogEntriesTracker getLogTracker()
  {
    return logTracker;
  }

  @Test
  public void test_log_entries()
  {
    LOG.info(String.format("[%s] The quick brown fox jumps over the lazy dog", LogId.ID001));
    LOG.warn(String.format("[%s] You can't always get what you want", LogId.ID002));
    assertLogEntry("quick", "fox", "dog");
    assertLogEntry("get");
    assertLogEntry(LogId.ID001);
    assertNoLogEntry(LogId.ID002, "fox");
    assertNoLogEntry(LogId.ID003);
  }
}
````

**Variant 2**: Static import of ``Junit4LogAssertions``

- Declare ``import static org.pfsw.julea.core.assertions.Junit4LogAssertions.*;``
- Declare a field *logTracker* annotated as ``@Rule`` using ``Junit4logbackTracker`` to initialize

````java
import static org.pfsw.julea.core.assertions.Junit4LogAssertions.*;
import org.junit.Rule;
import org.junit.Test;
import org.pfsw.julea.core.LogLevel;
import org.pfsw.julea.logback.junit4.junit4.Junit4logbackTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Junit4Test
{
  private static final Logger LOG = LoggerFactory.getLogger(Junit4Test.class);

  @Rule
  public Junit4logbackTracker logTracker = Junit4logbackTracker.track(Junit4Test.class);

  @Test
  public void test_log_entries()
  {
    LOG.info("The quick brown fox jumps over the lazy dog");
    assertLogEntry(logTracker, "quick", "fox", "dog");
    assertLogEntryMessage(logTracker, "^The.*jumps over.*"); // regex
    assertNoLogEntry(logTracker, "cat");
  }
}
````
