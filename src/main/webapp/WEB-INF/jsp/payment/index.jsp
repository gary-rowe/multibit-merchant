<%@include file="/WEB-INF/jspf/payment/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/payment/head.jspf" %>
</head>
<body>
<%@include file="/WEB-INF/jspf/payment/header.jspf" %>
<%@include file="/WEB-INF/jspf/payment/alert.jspf" %>
<%@include file="/WEB-INF/jspf/payment/content-header.jspf" %>
<div id="mbm-col1" class="col1">
  <div class="mbm-order-summary">
    <h2 class="centered">Request for Payment</h2>

    <div class="mbm-order-security">
      <p>For your security, here is the information you agreed with us during registration.</p>

      <p>Your code word is: <strong>aardvark</strong></p>

      <p>is your code image <img class="float-left" src="<c:url value="/images/mbm/icons/os_linux.gif" />"/></p>

      <p>&nbsp;</p>

      <p>Not what you expected? <strong>Stop right now!</strong></p>
    </div>
    <div class="mbm-item">
      <img class="mbm-item-thumbnail float-right" src="<c:url value="/images/catalog/items/2/thumbnail2.png" />"/>
      <p>Cryptonomicon, By Neal Stephenson</p>

      <p>Quantity: 1 at 3.25BTC (&euro;6.50)</p>
      <p>&nbsp;</p>
      <p>&nbsp;</p>

    </div>
    <p>To pay, you can click, drag or scan this swatch</p>

    <div id="mbm-order-swatch"></div>
    <button id="item-1" onclick="handleConfirmPaymentClick(this)"><fmt:message
      key="payment.page.payment-confirmed"/></button>

  </div>
</div>
<%@include file="/WEB-INF/jspf/payment/content-footer.jspf" %>

<%@include file="/WEB-INF/jspf/payment/footer.jspf" %>

<%@include file="/WEB-INF/jspf/payment/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/payment/mbm-scripts.jspf" %>
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
   * Handles an AlertMessage
   * @param message The decoded AlertMessage
   */
  function handleAlertMessage(message) {

    $("#alert").html(message.text);
    $("#alert").slideToggle("slow").toggleClass("active").delay(4000).slideToggle("slow").toggleClass("active");

  }

  /**
   * HTML5 drag event event handler
   */
  function dragStartHandler(event) {
    $.post("/mbm/api/v1/bitcoin/monitor/" + event.srcElement.id, function(data) {
      $("#bitcoin-monitor-output").html(data);
    });
  }


  // Initialisation

  console.log("Performing initial subscriptions.");

  // Subscribe to Alert feed
  $.atmosphere.subscribe(
    "<c:url value='/api/v1/alert/subscribe'/>",
    handleMessage,
    $.atmosphere.request = {transport: 'websocket'});

  // Get the swatch and register the address
  $.post('/mbm/api/v1/bitcoin/new-address',
    function(data) {
      console.log("Received callback");
      if (data == null || data == "") {
        alert("You need to be logged in to confirm an order");
      } else {
        // Show a swatch based on the bitcoin address (1pmG7fTVaVL1omx1TAgrGG2mNHbL4B1fb)
        var address = "/mbm/api/v1/bitcoin/swatch?address=" + data + "&amount=3.25&label=Bitcoin%20Books";
        $("#mbm-order-swatch").html("<img class='mbm-order-swatch' src='" + address + "' />");
      }
    });


</script>

</body>
</html>