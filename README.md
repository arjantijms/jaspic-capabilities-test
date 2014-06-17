jaspic-capabilities-test
========================

Special test branch for JMX based WebLogic 12.1.x container for a series of integration tests that test the capabilities of 
the JASPIC implementation of a Java EE Application Server.

This requires a client jar to be build from an existing WebLogic install via the following steps:

1. cd [WLS INSTALL DIR]/wlserver/server/lib
2. ln -s weblogic.jar weblogic-classes.jar
3. java -jar wljarbuilder.jar
4. mvn install:install-file -DgroupId=wlfullclient.jar -DartifactId=wlfullclient.jar -Dversion=12.1.2 -Dpackaging=jar -Dfile=wlfullclient.jar


To get started:

1. Adjust the installed location of WebLogic in containers.properties to match the location on your system
2. Run mvn clean verify -P arquillian-weblogic121-remote

