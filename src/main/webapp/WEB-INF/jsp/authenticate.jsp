<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
  <title>MultiBit Merchant Sign In</title>
  <!-- Simple OpenID Selector -->
  <link type="text/css" rel="stylesheet" href="/css/openid/openid.css"/>
  <script type="text/javascript" src="/js/min/jquery-1.2.6.min.js"></script>
  <script type="text/javascript" src="/js/openid/openid-jquery.js"></script>
  <script type="text/javascript" src="/js/openid/openid-en.js"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      openid.init('openid_identifier');
    });
  </script>
  <!-- /Simple OpenID Selector -->
  <style type="text/css">
      /* Basic page formatting */
    body {
      font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
    }
  </style>
</head>

<body>
<h2>MultiBit Merchant</h2>

<p>Do you already have an account on one of these sites? Click the logo to sign in with it here:</p>
<br/>

  <form name="oidf" action="/j_spring_openid_security_check" method="post" id="openid_form">
    <fieldset>
      <legend>Sign in or Create New Account</legend>
      <div id="openid_choice">
        <p>Please click your account provider:</p>

        <div id="openid_btns"></div>
      </div>
      <div id="openid_input_area">
        <input id="openid_identifier" name="openid_identifier" type="text" value="http://"/>
        <input id="openid_submit" type="submit" value="Sign-In"/>
      </div>
      <noscript>
        <p>OpenID is service that allows you to sign in to many different websites using a single identity.
          Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to
            get an OpenID enabled account</a>.</p>
      </noscript>
    </fieldset>
  </form>
</body>
</html>