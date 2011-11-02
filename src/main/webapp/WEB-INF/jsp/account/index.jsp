<%@include file="/WEB-INF/jspf/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body id="home">
<%@include file="/WEB-INF/jspf/header.jspf" %>
<%@include file="/WEB-INF/jspf/alert.jspf" %>

<div id="content">
  <div id="main">
    <div class="intro">
      <p>Welcome to your account page, ${emailAddress}!</p>

    </div>

  </div>

  <%@include file="/WEB-INF/jspf/sidebar.jspf" %>

</div>

<%@include file="/WEB-INF/jspf/footer.jspf" %>

</html>