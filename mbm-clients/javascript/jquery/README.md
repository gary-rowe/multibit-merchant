# MultiBit Merchant (jQuery client)

This module provides a simple MultiBit Merchant (MBM) client written in jQuery.

## Use case: A simple blog offering merchandise

A common situation on the web is accepting payment for branded merchandise offered on a blog. To achieve this
you need to have an instance of MBM running somewhere, and include a JavaScript link on your blog page, perhaps
as part of the right hand sidebar.

## Getting started

It is necessary that the whole MBM project has been built locally using

    mvn clean install

This will ensure that mbm/target/mbm-<version>.jar is present.

Once MBM has been built, execute the following statement in this module

    mvn clean exec:exec jetty:run

This will start the MBM platform as a standalone module, using the default
project settings. It will also start an instance of Jetty which will serve
the JavaScript files necessary to run the shopping cart.

To see it in action open a browser and navigate to

    http://localhost:8081/mbm-client/index.html

There you should see a simple example blog for a bookstore with an interactive shopping cart.

## Select some items



