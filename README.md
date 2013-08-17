jaspic-capabilities-test
========================

Series of integration tests that test the capabilities of the JASPIC implementation of a Java EE Application Server.

To get started:

1. copy /copy_me_do__containers.properties to /containers.properties
2. Adjust the locations of the installed application servers to match the location on your system
3. Run mvn --fail-at-end clean verify -P [select a profile] e.g mvn --fail-at-end clean verify -P arquillian-glassfish-embedded-3.1

Alternatively the Ant file runall.xml can be used to run the tests for more than one server in succession. Via the Ant file WebLogic can be started
and stopped automatically as part of the test. This is not possible via Maven (the Arquillian container doesn't support it). When using
only Maven then WebLogic needs to be started manually prior to starting the test.
