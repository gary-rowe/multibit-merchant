MultiBit Merchant
=================

Please refer to the main README.txt for the current state of the project and lines of development.

Description
-----------
This module provides the RESTful API and back-end services for an online shop.
The intention is that a particular client will use the web services to achieve
their objectives so there is little or no user interface.

Getting started
---------------

Build the project using

mvn clean package

Then run up the application as a standalone process with

java -jar target/mbm-0.0.1-SNAPSHOT.jar server mbm.yml

Then navigate to

http://localhost:8081/healthcheck

This will run the self-diagnostics and verify that the service is up and running.

Next steps
----------
To fully explore the RESTful API it is recommended that you now run up one of the dedicated clients
which provide a much richer user interface experience. Many of the clients automatically run up a
suitable version of this module so can be treated as a self-contained exploratory environment.

Advanced exploration
--------------------
If you are keen to explore the API without visual feedback, you can use curl to perform the necessary HTTP requests as
shown below for the various use cases.

Startup Graphic
---------------
The ASCII art for the startup banner was created using the online tool available at
http://www.webestools.com/ascii-text-generator-ascii-art-code-online-txt2ascii-text2ascii-maker-free-text-to-ascii-converter.html
