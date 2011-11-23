<%@include file="/WEB-INF/jspf/public/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/public/head.jspf" %>
  <link type="text/css" rel="stylesheet" href="<c:url value="/plugins/jquery/openid/openid.css"/>"/>
</head>
<body>
<%@include file="/WEB-INF/jspf/public/header.jspf" %>
<%@include file="/WEB-INF/jspf/public/alert.jspf" %>
<%@include file="/WEB-INF/jspf/public/content-header.jspf" %>
<div id="mbm-col1" class="col1">
  <div class="ui-widget">
    <p class="ui-widget-header">Authentication Required</p>

    <div class="ui-widget-content">

      <!-- TODO i18n -->
      <p>Do you already have an account on one of these sites? Click the logo to sign in with it here:</p>
      <br/>

      <form class="openid" method="post" action="<c:url value="/j_spring_openid_security_check"/>">
        <div class="ui-widget-content">
          <ul class="providers">
            <li class="openid" title="OpenID"><img src="<c:url value="/plugins/jquery/openid/images/openid.gif"/>"
                                                   alt="icon"/>
              <span><strong>http://{your-openid-url}</strong></span></li>
            <li class="direct" title="Google">
              <img src="<c:url value="/plugins/jquery/openid/images/google.gif"/>" alt="icon"/><span>https://www.google.com/accounts/o8/id</span>
            </li>
            <li class="direct" title="Yahoo">
              <img src="<c:url value="/plugins/jquery/openid/images/yahoo.gif"/>" alt="icon"/><span>http://yahoo.com/</span>
            </li>
            <li class="username" title="MyOpenID user name">
              <img src="<c:url value="/plugins/jquery/openid/images/myopenid.gif"/>" alt="icon"/><span>http://<strong>username</strong>.myopenid.com/</span>
            </li>
            <li class="username" title="AOL screen name">
              <img src="<c:url value="/plugins/jquery/openid/images/aol.gif"/>"
                   alt="icon"/><span>http://openid.aol.com/<strong>username</strong></span></li>
          </ul>
          <ul class="providers">
            <li class="username" title="Flickr user name">
              <img src="<c:url value="/plugins/jquery/openid/images/flickr.png"/>"
                   alt="icon"/><span>http://flickr.com/<strong>username</strong>/</span></li>
            <li class="username" title="Technorati user name">
              <img src="<c:url value="/plugins/jquery/openid/images/technorati.png"/>"
                   alt="icon"/><span>http://technorati.com/people/technorati/<strong>username</strong>/</span></li>
            <li class="username" title="Wordpress blog name">
              <img src="<c:url value="/plugins/jquery/openid/images/wordpress.png"/>" alt="icon"/><span>http://<strong>username</strong>.wordpress.com</span>
            </li>
            <li class="username" title="Blogger blog name">
              <img src="<c:url value="/plugins/jquery/openid/images/blogger.png"/>"
                   alt="icon"/><span>http://<strong>username</strong>.blogspot.com/</span></li>
            <li class="username" title="LiveJournal blog name">
              <img src="<c:url value="/plugins/jquery/openid/images/livejournal.png"/>" alt="icon"/><span>http://<strong>username</strong>.livejournal.com</span>
            </li>
            <li class="username" title="ClaimID user name">
              <img src="<c:url value="/plugins/jquery/openid/images/claimid.png"/>"
                   alt="icon"/><span>http://claimid.com/<strong>username</strong></span></li>
            <li class="username" title="Vidoop user name">
              <img src="<c:url value="/plugins/jquery/openid/images/vidoop.png"/>"
                   alt="icon"/><span>http://<strong>username</strong>.myvidoop.com/</span></li>
            <li class="username" title="Verisign user name">
              <img src="<c:url value="/plugins/jquery/openid/images/verisign.png"/>"
                   alt="icon"/><span>http://<strong>username</strong>.pip.verisignlabs.com/</span>
            </li>
          </ul>
        </div>
        <fieldset>
          <label for="openid_username">Enter your <span>Provider user name</span></label>

          <div><span></span><input type="text" name="openid_username"/><span></span>
            <input type="submit" value="Login"/></div>
        </fieldset>
        <fieldset>
          <!-- TODO Make this look nice -->
          <label for="openid_identifier">Enter your OpenID (example: <span>http://<strong>username</strong>.myopenid.com/</span>)</label>
          <input type="text" name="openid_identifier" />
          <input type="submit" value="<fmt:message key="login.login"/>"/>
          <noscript>
            OpenID is service that allows you to sign in to many different websites using a single identity.
              Find out <a href="http://openid.net/what/" target="_blank">more about OpenID</a> and <a
                href="http://openid.net/get/" target="_blank">how to get an OpenID enabled account</a>.
          </noscript>
        </fieldset>
      </form>
    </div>
  </div>
</div>
<%@include file="/WEB-INF/jspf/public/sidebar.jspf" %>
<%@include file="/WEB-INF/jspf/public/content-footer.jspf" %>

<%@include file="/WEB-INF/jspf/public/sidebar.jspf" %>
<%@include file="/WEB-INF/jspf/public/footer.jspf" %>

<%@include file="/WEB-INF/jspf/public/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/public/mbm-scripts.jspf" %>
<script type="text/javascript" src="<c:url value="/plugins/jquery/openid/openid.js"/>"></script>
<script type="text/javascript"> $(function() {
  $("form.openid:eq(0)").openid();
});</script>

</body>
</html>
