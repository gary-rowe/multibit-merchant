<!DOCTYPE html>
<html lang="en">
<head>
<#include "../includes/head.ftl">
</head>

<body>

<div class="container">
<#include "../includes/header.ftl">
<#include "../includes/navbar.ftl">
  <div class="row">

    <div class="span3">
      <h2>Lightweight defaults</h2>

      <h3>Rewritten base class</h3>

      <p>With Bootstrap 2, we've simplified the base class: <code>.alert</code> instead of <code>.alert-message</code>.
        We've also reduced the minimum required markup&mdash;no <code>&lt;p&gt;</code> is required by default, just the
        outer <code>&lt;div&gt;</code>.</p>
    </div>
    <div class="span9">
      <div class="page-header">
        <h1>News
          <small>What we do</small>
        </h1>
      </div>

      <!-- Headings & Paragraph Copy -->
      <div class="row">


        <div class="span9">
          <blockquote class="pull-right">
            <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante venenatis.</p>
            <small>Someone famous in <cite title="">Body of work</cite></small>
          </blockquote>

        </div>

        <div class="span9">
          <h3>Why do you do what you do?</h3>

          <p>Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient
            montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>

          <p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus,
            nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec sed odio dui.</p>
        </div>

        <div class="span9">
          <h3>Who are the people behind the company?</h3>

          <p>Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient
            montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>

          <p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus,
            nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec sed odio dui.</p>
        </div>

        <div class="span9">
          <h3>What kind of people will I be working with/buying from?</h3>

          <p>Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient
            montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>

          <p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus,
            nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec sed odio dui.</p>
        </div>

        <div class="span9">
          <h3>What does your company stand for?</h3>

          <p>Nullam quis risus eget urna mollis ornare vel eu leo. Cum sociis natoque penatibus et magnis dis parturient
            montes, nascetur ridiculus mus. Nullam id dolor id nibh ultricies vehicula ut id elit.</p>

          <p>Vivamus sagittis lacus vel augue laoreet rutrum faucibus dolor auctor. Duis mollis, est non commodo luctus,
            nisi erat porttitor ligula, eget lacinia odio sem nec elit. Donec sed odio dui.</p>
        </div>


        <!-- Misc Elements -->

      </div>
      <!-- /row -->

    </div>

  </div>

<#include "../includes/footer.ftl">

</div>
<!-- /container -->

<#include "../includes/cdn-scripts.ftl">

</body>
</html>