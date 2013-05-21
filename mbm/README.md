# MultiBit Merchant

Please refer to the main README.txt for the current state of the project and lines of development.

## Description

This module provides the RESTful API and back-end services for an online shop.
The intention is that a particular client will use the web services to achieve
their objectives so there is little or no user interface.

## Getting started from command line

Build the project using

    mvn clean package

Then run up the application as a standalone process with

    java -jar target/mbm-develop-SNAPSHOT.jar server mbm.yml

If all is well, you'll see a large MBM ASCII art appear and some logging notes.

Then navigate to

    http://localhost:8081/healthcheck

This will run the self-diagnostics and verify that the service is up and running.

## Getting started from within IDE with runtime configuration

The main entry point to MBM is through MultiBitMerchantService.main()

Configure the runtime environment to execute in the MBM module folder (e.g. same as this file)
and provide the following parameters

    server mbm.yml

## Next steps

To fully explore the RESTful API it is recommended that you now run up one of the dedicated clients
which provide a much richer user interface experience. Many of the clients automatically run up a
suitable version of this module so can be treated as a self-contained exploratory environment.

Take a look at:

* [MultiBit Store](https://github.com/gary-rowe/MultiBitStore)

## Advanced exploration

If you are keen to explore the API without visual feedback, you can use curl to perform the necessary HTTP requests as
shown below for the various use cases.

## Where does the ASCII art come from?

The ASCII art for the startup banner was created using the online tool available at
[Webestools][http://www.webestools.com/ascii-text-generator-ascii-art-code-online-txt2ascii-text2ascii-maker-free-text-to-ascii-converter.html]
with a font of Tiza