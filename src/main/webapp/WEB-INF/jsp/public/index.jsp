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
    <p class="ui-widget-header">Recommended Books</p>

    <div>
      <p>${greeting}</p>

      <p>hello!</p>

      <!-- TODO make this driven by the model -->
      <div class="mbm-item ui-widget-content ui-corner-all">
        <a href="#" class="mbm-item-link">Cryptonomicon, By Neal
          Stephenson</a>
        <img class="mbm-item-thumbnail float-right" src="<c:url value="/images/catalog/items/2/thumbnail2.png" />"/>

        <p>'A brilliant patchwork of codebreaking mathematicians and their descendants who are striving to create a
          datahaven in the Philippines...trust me on this one' Guardian</p>

        <p>3.25BTC (&euro;6.50)</p>

        <button id="item-1" class="mbm-add-to-basket" onclick="handleAddToBasketClick(this)"><fmt:message key="catalog.page.add-to-basket"/></button>
      </div>

      <div class="mbm-item ui-widget-content ui-corner-all">
        <a href="#" class="mbm-item-link">A Year in Provence, By
          Peter Mayle</a>
        <img class="mbm-item-thumbnail float-right" src="<c:url value="/images/catalog/items/1/thumbnail1.png" />"/>

        <p>Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's
          bestselling, much-loved account of 'A Year In Provence'.</p>

        <p>1.95BTC (&euro;3.90)</p>

        <button id="item-2" class="mbm-add-to-basket"onclick="handleAddToBasketClick(this)"><fmt:message key="catalog.page.add-to-basket"/></button>
      </div>
      <div class="ui-widget-content ui-corner-all">
        <a href="#" class="mbm-item-link">Plumbing and Central
          Heating, By Mike Lawrence</a>
        <img class="mbm-item-thumbnail float-right" src="<c:url value="/images/catalog/items/3/thumbnail3.png" />"/>

        <p>This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation,
          from mending a leaking tap to installing a new shower unit.</p>

        <p>1.95BTC (&euro;3.90)</p>

        <button id="item-3" class="mbm-add-to-basket" onclick="handleAddToBasketClick(this)"><fmt:message key="catalog.page.add-to-basket"/></button>
      </div>

      <div class="mbm-item ui-widget-content ui-corner-all">
        <a href="#" class="mbm-item-link">The Quantum Thief, By Hannu
          Rajaniemi</a>
        <img class="mbm-item-thumbnail float-right" src="<c:url value="/images/catalog/items/4/thumbnail4.png" />"/>

        <p>The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard
          Morgan.</p>

        <p>1.95BTC (&euro;3.90)</p>

        <button id="item-4" class="mbm-add-to-basket" onclick="handleAddToBasketClick(this)"><fmt:message key="catalog.page.add-to-basket"/></button>
      </div>

      <div class="mbm-item ui-widget-content ui-corner-all">
        <a href="#" class="mbm-item-link">The Complete Works of
          Emily Dickinson, Edited by Thomas H Johnson</a>
        <img class="mbm-item-thumbnail float-right" src="<c:url value="/images/catalog/items/5/thumbnail5.png" />"/>

        <p>The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's
          poems.</p>

        <p>1.95BTC (&euro;3.90)</p>

        <button id="item-5" class="mbm-add-to-basket" onclick="handleAddToBasketClick(this)"><fmt:message key="catalog.page.add-to-basket"/></button>
      </div>
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

  function handleAddToBasketClick(event) {
    console.log("Adding item-1 to shopping basket");
    $("#item-1-order").slideToggle("slow").toggleClass("active");
  }

  function handleConfirmOrder(event) {
    console.log("Confirming order");
    // Request an address
    $.post('/mbm/api/v1/bitcoin/new-address',
      function(data) {
        console.log("Received callback");
        if (data == null || data=="") {
          alert("You need to be logged in to confirm an order");
        } else {
          // Show a swatch based on the bitcoin address (1pmG7fTVaVL1omx1TAgrGG2mNHbL4B1fb)
          $("#mbm-order-swatch").html("<img src='/mbm/api/v1/bitcoin/swatch?address=&'+data+'amount=1&label=Your%20items' />");
        }
      });
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
    $(this).button({
      icons: {
        primary: "ui-icon-plus",
        secondary: "ui-icon-triangle-1-s"
      },
      text: false
    });
  });

</script>

</body>
</html>
