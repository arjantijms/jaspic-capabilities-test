jaspic-capabilities-test
========================

Special test branch for JMX based WebLogic 12.1.x container for a series of integration tests that test the capabilities of 
the JASPIC implementation of a Java EE Application Server.

This requires the WebLogic jar "wlthint3client.jar" to be put on the class path of Arquillian. One way to do this is:

1. cd [WLS INSTALL DIR]/wlserver/server/lib
2. mvn install:install-file -DgroupId=wlthint3client.jar -DartifactId=wlthint3client.jar -Dversion=12.1.2 -Dpackaging=jar -Dfile=wlthint3client.jar


To get started:

1. Adjust the installed location of WebLogic in containers.properties to match the location on your system
2. Run mvn clean verify -P arquillian-weblogic121-remote

