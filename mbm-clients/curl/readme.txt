cURL client for MBM
===================

This provides a set of cURL commands that work against the MBM RESTful API. To get them to work you will need to run up
a copy of MBM using mvn clean jetty:run.

A useful tutorial for cURL is available here: http://curl.haxx.se/docs/httpscripting.html

Getting SSL to work
-------------------
All communication with MBM is done over SSL to maintain the highest level of security for traffic.