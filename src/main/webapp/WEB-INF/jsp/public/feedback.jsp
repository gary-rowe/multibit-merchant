<%@include file="/WEB-INF/jspf/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>
<%@include file="/WEB-INF/jspf/header.jspf" %>
<%@include file="/WEB-INF/jspf/alert.jspf" %>
<div id="mbm-content" class="ui-widget-header">
  <div class="ui-widget-content">
    <p>Welcome to the feedback page, ${emailAddress}!</p>
  </div>
</div>

<%@include file="/WEB-INF/jspf/sidebar.jspf" %>
<%@include file="/WEB-INF/jspf/footer.jspf" %>

<%@include file="/WEB-INF/jspf/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/mbm-scripts.jspf" %>

<script type="text/javascript" src="<c:url value="/js/mbm/mbm.js"/>"></script>
</body>
</html>
