# MultiBit Merchant (jQuery client)

This module provides a simple MultiBit Merchant (MBM) client written in jQuery.

## Use case: A simple blog offering merchandise

A common situation on the web is accepting payment for branded merchandise offered on a blog. To achieve this
you need to have an instance of MBM running somewhere, and include a JavaScript link on your blog page, perhaps
as part of the right hand sidebar.

## Getting started within an IDE

You will need to run up the MBM platform as a separate process. Full instructions are in the mbm/README.md file.

Once MBM is running, you can run up this module using Jetty:

    mvn clean jetty:run

To see it in action open a browser and navigate to

    http://localhost:8080/mbm-client/index.html

There you should see a simple example blog for a bookstore with an interactive shopping cart.

## Getting started outside of an IDE

Make sure you have built the project, or have access to the MBM platform JAR file. Assuming you're on the
command line within the Maven project, start the MBM process using

    cd <project-root>
    mvn clean install
    java -jar mbm/target/mbm-<version>.jar server mbm.yml

Choose appropriate values for <project-root> and <version>. If you are on the develop branch then

    java -jar mbm/target/mbm-develop-SNAPSHOT.jar server mbm.yml

will do the trick.

This will start the MBM platform as a standalone module, using the default
project settings: port 8080 (public) and port 8081 (admin). It will also start an instance of Jetty
which will serve the JavaScript files necessary to run the shopping cart.


## Select some items



