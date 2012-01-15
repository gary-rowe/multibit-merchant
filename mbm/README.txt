MultiBit Merchant
=================

Please refer to the main README.txt for the current state of the project and lines of development.

Description
-----------
This module provides the RESTful API and back-end services for an online shop. The intention is that a particular client
will use the web services to achieve their objectives so there is little or no user interface.

Getting started
---------------

Build and run up a local web server from the command line with the following command

mvn clean jetty:run

Then navigate to

http://localhost:8080/mbm/api/v1/time/now

The response will contain the current time in ISO8601 format for the UTC time zone. Press CTRL+C to stop the server.

To fully explore the RESTful API it is recommended that you now run up one of the dedicated clients which provide a much
richer user interface experience. Many of the clients automatically run up a suitable version of this module so can be
treated as a self-contained exploratory environment.

Advanced exploration
--------------------
If you are keen to explore the API without visual feedback, you can use curl to perform the necessary HTTP requests as
shown below for the various use cases.

