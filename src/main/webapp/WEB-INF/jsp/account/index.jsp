<%@include file="/WEB-INF/jspf/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>
<%@include file="/WEB-INF/jspf/header.jspf" %>
<%@include file="/WEB-INF/jspf/alert.jspf" %>
<%@include file="/WEB-INF/jspf/content-header.jspf" %>
<div id="mbm-col1" class="col1">
  <div class="ui-widget">
    <p class="ui-widget-header">Your Account</p>

    <div class="ui-widget-content">
      <p>Welcome to your account page, ${emailAddress}!</p>

      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin non nulla libero. Vivamus lobortis dolor ut
        lectus iaculis vel vestibulum lectus sagittis. Duis id mollis velit. Vivamus faucibus scelerisque arcu. Vivamus
        ullamcorper posuere lectus, nec sodales dolor suscipit vel. Nullam pulvinar luctus viverra. Donec consequat quam
        at libero viverra molestie. Sed et semper nisi. Fusce eu erat elit. Vivamus non est metus, vel pharetra nulla.
        Praesent ultrices nisi et libero cursus eleifend. Etiam condimentum gravida enim scelerisque pulvinar.
        Pellentesque dignissim pharetra ligula venenatis volutpat.

    </div>
  </div>
</div>
<%@include file="/WEB-INF/jspf/sidebar.jspf" %>
<%@include file="/WEB-INF/jspf/content-footer.jspf" %>
<%@include file="/WEB-INF/jspf/footer.jspf" %>

<%@include file="/WEB-INF/jspf/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/mbm-scripts.jspf" %>

</body>
</html>