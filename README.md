caliper-java-example
====================
NOTE: THESE MATERIALS ARE FOR IMS CONTRIBUTING MEMBERS ONLY. THEY MAY NOT BE RELEASED UNTIL APPROVED BY IMS GLOBAL.

This is a sample WebApp that utilizes the Caliper Java sensor.

## Installation

* Install Java 7 on your machine
* Download a binary distribution of the core module: apache-tomcat-7.0.47.tar.gz from http://tomcat.apache.org/download-70.cgi
* Install Tomcat - this involves uncompressing the archive and moving it to an appropriate location. E.g.:
```
sudo mv ~/Downloads/apache-tomcat-7.0.47 /usr/local/tomcat
```
* Make all scripts executable:
```
sudo chmod +x /usr/local/tomcat/bin/*.sh
```
* Fire up Tomcat
```
cd /usr/local/tomcat
./bin/startup.sh
```
* Build the example
```
cd .../caliper-java-example/
mvn clean package
```
* Copy the WAR into tomcat/webapps
```
cp ./target/caliper-java-example.war /usr/local/tomcat/webapps/
```
* In a browser, navigate to http://localhost:8080/caliper-java-example/testservlet

©2014 IMS Global Learning Consortium, Inc. All Rights Reserved.  Trademark Information - http://www.imsglobal.org/copyright.html
