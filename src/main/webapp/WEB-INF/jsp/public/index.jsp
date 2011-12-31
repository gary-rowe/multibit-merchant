<%@include file="/WEB-INF/jspf/public/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/public/head.jspf" %>
</head>
<body>
<%@include file="/WEB-INF/jspf/public/header.jspf" %>
<%@include file="/WEB-INF/jspf/public/alert.jspf" %>
<%@include file="/WEB-INF/jspf/public/content-header.jspf" %>
<div id="mbm-col1" class="col1">
  <div class="ui-widget">
    <p class="ui-widget-header">${greeting}</p>

    <div id="catalog-item-search-output"></div>
  </div>

</div>
<%@include file="/WEB-INF/jspf/public/sidebar.jspf" %>
<%@include file="/WEB-INF/jspf/public/content-footer.jspf" %>

<%@include file="/WEB-INF/jspf/public/footer.jspf" %>

<%@include file="/WEB-INF/jspf/public/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/public/mbm-scripts.jspf" %>

<script type="text/javascript" src="<c:url value="/js/mbm/mbm.js"/>"></script>

<script id="twitterTemplate" type="text/x-jquery-tmpl">
  <li><img alt='\${fromUser}' title='\${fromUser}' src='\${profileImageUrl}' width='48' height='48'>

    <div><c:out value='\${text}'/></div>
  </li>
</script>
<script type="text/javascript">

  /*
   * This is the primary callback for HTTP responses
   * @param response
   */
  function handleMessage(response) {
    $.atmosphere.log('info', ["response.state: " + response.state]);
    $.atmosphere.log('info', ["response.transport: " + response.transport]);

    if (response.transport != 'polling' && response.state != 'connected' && response.state != 'closed') {
      $.atmosphere.log('info', ["response.responseBody: " + response.responseBody]);
      if (response.status == 200) {
        var data = response.responseBody;

        if (data) {

          if (data.substring(0, 2) == "<!") {
            console.log("Response is initial suspend text - ignoring.");
          } else {
            try {
              var message = $.parseJSON(data);
              if (message.topic == "AlertMessage") {
                handleAlertMessage(message);
                return;
              }
              if (message[0].topic == "TwitterMessage") {
                handleTwitterMessage(message);
                return;
              }
              console.log("Unknown message topic: " + message.topic);
            } catch (error) {
              console.log("An error ocurred: " + error);
            }

          }
        }
      }
    }

  }

  /**
   * Handles a TwitterMessage
   * @param message The decoded TwitterMessage
   */
  function handleTwitterMessage(message) {

    var visible = $('#twitterPlaceHolder').is(':visible');

    if (message.length > 0 && visible) {
      $("#twitterPlaceHolder").fadeOut();
    }

    $("#twitterMessages").html($("#twitterTemplate").tmpl(message)).fadeIn();

  }

  /**
   * Handles an AlertMessage
   * @param message The decoded AlertMessage
   */
  function handleAlertMessage(message) {

    $("#alert").html(message.text);
    $("#alert").slideToggle("slow").toggleClass("active").delay(4000).slideToggle("slow").toggleClass("active");

  }

  function handleAddToBasketClick(event) {
    console.log("Adding item-1 to shopping basket");
    $("#item-1-order").slideToggle("slow").toggleClass("active");
  }

  function handleConfirmOrder(event) {
    window.location = "<c:url value="/payment.html"/>";
  }

  // Initialisation

  console.log("Performing initial subscriptions.");

  /* Subscribe to Twitter feed */
  <%--$.atmosphere.subscribe(--%>
  <%--"<c:url value='/api/v1/twitter/subscribe'/>",--%>
  <%--handleMessage,--%>
  <%--$.atmosphere.request = {transport: 'websocket'});--%>

  /* Subscribe to Alert feed */
  $.atmosphere.subscribe(
    "<c:url value='/api/v1/alert/subscribe'/>",
    handleMessage,
    $.atmosphere.request = {transport: 'websocket'});

  $("button").each(function() {
    $(this).button();
  });

  // TODO Serve this from the app
  var searchTemplate="<div class='mbm-item ui-widget-content ui-corner-all'><a href='#' class='mbm-item-link'>{title}</a><img class='mbm-item-thumbnail float-right' src='{imgThumbnailUri}'/></a><p>{summary}</p><p>3.25BTC (&euro;6.50)</p><button id='item-1' class='mbm-add-to-basket' onclick='handleAddToBasketClick(this)'>Add to basket</button></div>";

  $(document).ready(function() {
    // Populate the main page
    $.get('/mbm/api/v1/catalog/item/search',
      function(data) {
        var results = data.results;
        for (var i=0; i<results.length; i++) {
          var html = searchTemplate.supplant(results[i]);
          $('#catalog-item-search-output').append(html);
        }
      });

  });

</script>

</body>
</html>
