# MultiBit Merchant (jQuery client)

This module provides a simple MultiBit Merchant (MBM) client written in jQuery.

## Use case: A simple blog offering merchandise

A common situation on the web is accepting payment for branded merchandise offered on a blog. To achieve this
you need to have an instance of MBM running somewhere, and include a JavaScript link on your blog page, perhaps
as part of the right hand sidebar.

## Getting started

Follow the instructions in the main README.md to build and run up the MBM platform. This handles all the back
end operations, leaving the JavaScript to handle the front end.

Once MBM is running, execute the following statements in this module

    mvn clean jetty:run

then open a browser to

    http://localhost:8080/mbm-client/index.html

and you should see a simple example blog for a bookstore with an interactive shopping cart.

## Select some items



