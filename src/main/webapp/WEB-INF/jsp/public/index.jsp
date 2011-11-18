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
    <p class="ui-widget-header">Today's Top Offer</p>

    <div class="ui-widget-content">
      <p>Welcome to the catalog page, ${emailAddress}!</p>

      <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin non nulla libero. Vivamus lobortis dolor ut
        lectus iaculis vel vestibulum lectus sagittis. Duis id mollis velit. Vivamus faucibus scelerisque arcu. Vivamus
        ullamcorper posuere lectus, nec sodales dolor suscipit vel. Nullam pulvinar luctus viverra. Donec consequat quam
        at libero viverra molestie. Sed et semper nisi. Fusce eu erat elit. Vivamus non est metus, vel pharetra nulla.
        Praesent ultrices nisi et libero cursus eleifend. Etiam condimentum gravida enim scelerisque pulvinar.
        Pellentesque dignissim pharetra ligula venenatis volutpat.

      <p>Quisque pretium augue sit amet nisl feugiat mollis. Phasellus at enim non justo malesuada fermentum. Lorem
        ipsum dolor sit amet, consectetur adipiscing elit. Curabitur tristique ipsum quis augue scelerisque non accumsan
        nisi sagittis. Vivamus facilisis pulvinar nunc eget rhoncus. Curabitur lorem risus, lacinia et consequat sit
        amet, eleifend pulvinar nibh. Sed pulvinar tincidunt nunc, ut porttitor augue pharetra eu. Pellentesque non
        velit elit, tempor pharetra risus.</p>

      <p>Nullam quis ipsum odio, eleifend euismod lorem. Duis at lacus ligula. Nam porta, tortor nec molestie euismod,
        urna mauris pellentesque sem, vitae euismod enim purus vitae nulla. Praesent congue sagittis mi et pellentesque.
        Etiam pulvinar pretium eleifend. Ut at elit sit amet felis laoreet mollis ultricies id dolor. Phasellus
        fermentum est quis quam consectetur vitae euismod lectus dignissim. Aliquam pretium, augue quis pulvinar
        molestie, sapien tortor vulputate lectus, congue accumsan sem lacus at quam. Maecenas viverra quam ac libero
        mollis egestas. Donec id volutpat mauris. Pellentesque ut velit metus. Praesent vulputate sapien cursus sem
        porttitor imperdiet. Proin vitae risus nibh, a luctus tortor. Suspendisse egestas ipsum id leo eleifend
        sagittis. Integer vulputate nibh at tellus porttitor rhoncus.</p>


    </div>
  </div>
</div>
<%@include file="/WEB-INF/jspf/sidebar.jspf" %>
<%@include file="/WEB-INF/jspf/content-footer.jspf" %>

<%@include file="/WEB-INF/jspf/footer.jspf" %>

<%@include file="/WEB-INF/jspf/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/mbm-scripts.jspf" %>

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
    $("#alert").slideToggle("slow").toggleClass("active").delay(2000).slideToggle("slow").toggleClass("active");

  }

  console.log("Performing initial subscriptions.");

  /* Subscribe to Twitter feed */
  $.atmosphere.subscribe(
    "<c:url value='/api/v1/pubsub/twitter'/>",
    handleMessage,
    $.atmosphere.request = {transport: 'websocket'});

  /* Subscribe to Alert feed */
  $.atmosphere.subscribe(
    "<c:url value='/api/v1/pubsub/alert'/>",
    handleMessage,
    $.atmosphere.request = {transport: 'websocket'});

</script>

</body>
</html>
