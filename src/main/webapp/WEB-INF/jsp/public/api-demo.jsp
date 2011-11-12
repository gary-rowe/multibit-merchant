<%@include file="/WEB-INF/jspf/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/head.jspf" %>
</head>
<body>
<h2>MultiBit Merchant API demonstrator</h2>

<div id="tabs" class="hidden">
  <ul>
    <li><a href="#tab-1">Alerts</a></li>
    <li><a href="#tab-2">Time</a></li>
  </ul>
  <div id="tab-1">
    <p>The Alert API provides notification text that a customer should pay attention to.</p>
    <table>
      <colgroup>
        <col span="1" style="width:10%"/>
        <col span="1" style="width:40%"/>
        <col span="1" style="width:40%"/>
        <col span="1" style="width:10%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a href="http://localhost:8080/mbm/api/v1/alert/echo/Hello">Hello</a></td>
        <td>Echo an alert as a plain string</td>
        <td></td>
        <td></td>
      </tr>
    </table>
    <p></p>
  </div>
  <div id="tab-2">
    <p>The Time API provides access to various server time facilities.</p>
    <table>
      <colgroup>
        <col span="1" style="width:10%"/>
        <col span="1" style="width:40%"/>
        <col span="1" style="width:40%"/>
        <col span="1" style="width:10%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a href="http://localhost:8080/mbm/api/v1/time">Now UTC</a></td>
        <td>Echo the current server time in UTC as a plain string</td>
        <td></td>
        <td></td>
      </tr>
      <tr>
        <td><a id="json-time" href="#">Update time</a></td>
        <td>Echo the current server time as part of a JSON response</td>
        <td><code>$("#json-time").click($.getJSON(
          "http://localhost:8080/mbm/api/v1/time/extended",
          null,
          function (data) {
          $("#json-time-output").html("yyyy-MM-dd="+data.year+"-"+data.month+"-"+data.day);
          })
          );</code>
        </td>
        <td id="json-time-output"></td>
      </tr>
    </table>

    <p>
    </p>
  </div>
</div>
<%@include file="/WEB-INF/jspf/footer.jspf" %>

<%@include file="/WEB-INF/jspf/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/mbm-scripts.jspf" %>

<script type="text/javascript" src="<c:url value="/js/mbm/mbm.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/mbm/demo-time.js"/>"></script>
<script type="text/javascript">

  $(document).ready(function() {
    // Apply jQueryUI tabs
    $("#tabs").tabs();

  });

</script>
</body>
</html>
