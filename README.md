## Current build status

[![Build Status](https://travis-ci.org/gary-rowe/MultiBitMerchant.png?branch=develop)](https://travis-ci.org/gary-rowe/MultiBitMerchant)

## The vision
MultiBit Merchant is trading, reinvented. It is designed to minimise the amount of time you have to spend looking after it - most of it is fully automatic. It is intended to free you from a traditional working life and introduce you to a different approach. 

## What is it?
MultiBit Merchant is a complete online merchant platform designed for use with Bitcoin, an internet currency that has many unique features. To find out more about Bitcoin [you should visit LoveBitcoins.org](http://lovebitcoins.org) which provides a lot of useful introductory information. 

MultiBit Merchant is part of the MultiBit ecosystem of applications to help build the infrastructure of the Bitcoin economy. To find out more [you should visit MultiBit.org](http://multibit.org)

## What can I do with it?

There are many possibilities, but here are a few that have been designed for:

* Trying out a new entrepreneurial idea without having to register and pay for a payment gateway subscription first
* Running an online shop (sell your stuff direct to a global market for bitcoins)
* Running an online fulfilment house (take online orders and send fulfilment orders to your local distribution team)
* Paying developers to contribute to your open source project (every successful pull gets a Bitcoin bounty)
* Providing a paid-for upload/share/download service (get paid for helping people share stuff)
* Providing a cost-effective platform from which a group of local businesses can reach their customers (help your local takeaways provide a combined service, scale this up to more towns)
* Providing a Bitcoin paywall for your premium blog content (provide temporary access to an article for download)
* Running an international escrow service (act as a trusted third party to facilitate international trade between small businesses)
* Providing a last minute booking or reservation service offering zero chargeback risk to suppliers that could include airlines, hotels, holiday home rentals etc. 

## Features for merchants

Bitcoin offers many advantages, particularly in the area of international payments. MultiBit Merchant is intended to
help you build up Bitcoin-only supply chains so that you can run your entire business using Bitcoin.

* Instant set up and operation - no registration
* Very low transaction costs (*much* lower than credit cards or PayPal)
* No chargebacks
* Instant payment settlement (just like cash)
* Inexpensive to operate (server running costs and an SSL certificate [free from StartSSL] are all that is required)
* No registration required (anyone, anywhere can use it)
* Secure by design (no private keys on server)
* Simple to configure
* Fast bulk upload of catalogue items (as descriptions, or as digital products)
* Online order processing (customers can make their orders and review them later)
* Online delivery processing (you receive updates on new orders and fulfilments)
* Online fulfilment for digital products (sell your music right now)
* Online accounting in bitcoins to 8dp (reports can be sent direct to you and your accountant covering all aspects of the business operations)
* Social network support (you and your customers can receive updates through email, Twitter, Facebook, Google+ and so on)
* Multiple languages supported (for your site and your products) 

## Features for IT specialists

* Designed for long-term reliable automatic operation
* Main platform runs as a standalone web application on a Java6 JVM (no application container required)
* All requests and responses are stateless and cached
* All components designed to scale horizontally to allow larger sites to scale up and down as necessary
* Support for encrypted offsite backups and restores and schema updates
* Restricted administration access

## Which branch?

Use `master` for the latest production release. Use `develop` for the latest release candidate.

At present, given the pre-alpha nature of the project it is best to start with `develop` until the 0.1.0 milestone is
 reached.

## Which version?

The version numbers follow the usual Maven patter of Major(x).Minor(y).Revision(z) and should be interpreted as:

z - covers a bug fix that does not affect any public facing API
y - a new feature or bug fix that affects the public facing API
x - a major update that involves a complete rewrite and breaking changes all round

To cover the early work where everything is all up in air, the following can be used to determine how stable the
current release is:

0.0.z - Pre-alpha. Expect bugs and major rewrites of everything.
0.1.z - Alpha. Expect bugs, but it should sort of look OK and have most technology in place.
0.y.z - Beta. Expect minor bugs, and a good user experience overall.
1.y.z - Release. Should be fully operational and fit for customers to use.

## Bitcoinj dependency is broken - for good reasons

The current Bitcoinj library is not available through a standard Nexus repository for security reasons.

Before you think that this is some rubbish that you can safely ignore please bear in mind that Bitcoinj is a
financial library that is *capable of spending real money*. You want to be sure that no-one between you and their
repository has an opportunity to change the code. Further, you want to be sure that any of the libraries that it
pulls in have not been corrupted (a "[dependency-chain attack](http://gary-rowe.com/agilestack/2013/07/03/preventing-dependency-chain-attacks-in-maven/)").

The best way to ensure that you have the correct Bitcoinj library is to build it locally on a trusted machine and then
deploy it to a trusted repository under your control. To accomplish this, you should [follow the Bitcoinj build instructions](https://code.google.com/p/bitcoinj/wiki/UsingMaven).

### Latest update

The [BitcoinJ Enforcer Rules](https://github.com/gary-rowe/BitcoinjEnforcerRules) project provides the necessary
protection so that you can include BitcoinJ directly in your Maven projects using the traditional approach. However,
due to time constraints it is not yet available.

## Getting started
Assuming that you're familiar with Bitcoin and the benefits that it can bring to your online business venture, 
then your next step depends on your technical background. 

[Installation instructions for merchants](https://github.com/gary-rowe/MultiBitMerchant/wiki/Getting-started-for-merchants)

[Installation instructions for developers](https://github.com/gary-rowe/MultiBitMerchant/wiki/Getting-started-for-developers)
