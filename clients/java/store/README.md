# MultiBit Store

This module is an online shop built with the following:

* Java
* MBM - Providing the heavy lifting for back end merchant operations
* Dropwizard - Serves HTML and JavaScript, intermediates with the MBM platform
* Bootstrap - A JavaScript framework providing a simple CSS scheme
* jQuery - A JavaScript framework providing a variety of utilities

It is intended as a general-purpose online shop, but also as a demonstrator of the MBM platform.

## Notation

<project root> - The root directory of the project as checked out through git

All commands will work on *nix without modification, use \ instead of / for Windows.

## Getting started

First you must build the MBM project using Maven

    cd <project root>
    mvn clean install

You will need to have MBM running in a separate process. From the console you can do the following

    cd <project root>/mbm
    java -jar target/mbm-develop-SNAPSHOT.jar server mbm.yml

Finally, you will need to start the Dojo Store

## Where does the ASCII art come from?

The ASCII art for the startup banner was created using the online tool available at
[Webestools][http://www.webestools.com/ascii-text-generator-ascii-art-code-online-txt2ascii-text2ascii-maker-free-text-to-ascii-converter.html]
with a font of Tiza