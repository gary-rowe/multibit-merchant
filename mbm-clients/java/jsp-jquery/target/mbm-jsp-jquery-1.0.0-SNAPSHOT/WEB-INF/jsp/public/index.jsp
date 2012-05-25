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
    <div id="item-detail"></div>
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

<script id="searchItemTemplate" type="text/x-jquery-tmpl">
  <div id='item-\${id}' class='mbm-item ui-widget-content ui-corner-all'>
    <a href='#' onclick='handleItemDetailClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")' class='mbm-item-link'>\${title}</a>
    <a href='#' onclick='handleItemDetailClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")'><img
      class='mbm-item-thumbnail float-right' src='\${imgThumbnailUri}'/></a>

    <p>Get it by <strong>\${offeredDeliveryDate}</strong> if you order it today.</p>

    <p><strong>\${btcPrice} BTC</strong> (\${localSymbol} \${localPrice})</p>

    <button onclick='handleAddToCartClick(\${id},1)'>Add to cart</button>
    <button onclick='handleAddToCartClick(\${id},0)'>Remove from cart</button>
  </div>
</script>

<script id="itemDetailTemplate" type="text/x-jquery-tmpl">
  <div class='mbm-item ui-widget-content ui-corner-all'>
    <a href='#' onclick='handleAddToBasketClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")' class='mbm-item-link'>\${title}</a>
    <a href="#" onclick='handleAddToBasketClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")'><img
      class='mbm-item-thumbnail float-right' src='\${imgThumbnailUri}'/></a>

    <p>Get it by <strong>\${offeredDeliveryDate}</strong> if you order it today.</p>

    <p><strong>\${btcPrice} BTC</strong> (\${localSymbol} \${localPrice})</p>

    <button onclick='handleAddToCartClick(\${id},1)'>Add to cart</button>
    <button onclick='handleAddToCartClick(\${id},0)'>Remove from cart</button>

  </div>
</script>

<script id="cartTotalTemplate" type="text/x-jquery-tmpl">
  <p><span class="align-right"><strong>Total: \${btcTotal} BTC (\${localSymbol} \${localTotal})</strong></span></p>

  <p>Total includes tax and postage</p>
  <button id="confirm-order" onclick="handleConfirmOrder(this)"><fmt:message
    key="catalog.page.confirm-order"/></button>
</script>

<script id="cartItemTemplate" type="text/x-jquery-tmpl">
  <div id='cart-item=\${id}' class='mbm-item ui-widget-content ui-corner-all'>
    <p><a href='#' onclick='handleItemDetailClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")' class='mbm-item-link'>\${title}</a>
    </p>

    <p><input id='qty=\${id}' value='\${quantity}' size='2'>&nbsp;@&nbsp;\${btcPrice}&nbsp;BTC<span class='ui-icon-closethick'
                                                                             onclick='handleRemoveItem("\${id}")'>Word</span>
    </p>
  </div>
</script>

<script type="text/javascript">

  function handleAddToCartClick(id, quantity) {
    console.log("Adding item to cart");
    $.post(
      '/mbm/api/v1/cart',
      {
        itemId:id,
        quantity:quantity,
        token:'df575838-94dd-4b6b-8c86-8fc2371bd883'
      },
      function (data) {
        var cartItems = data.cartItemSummaries;
        $('#cart-items').html("");
        for (var i = 0; i < cartItems.length; i++) {
          $('#cart-items').append($("#cartItemTemplate").tmpl(cartItems[i]));
        }
        $('#cart-total').html($("#cartTotalTemplate").tmpl(data));
      });
  }

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
    console.log("Received alert message");
    $("#alert").html(message.text);
    $("#alert").slideToggle("slow").toggleClass("active").delay(4000).slideToggle("slow").toggleClass("active");

  }

  function handleItemDetailClick(uri) {
    console.log("Requesting detail");
    $.get(uri,
      function (data) {
        $("#catalog-item-search-output").fadeOut("fast");
        $("#item-detail").html($("#itemDetailTemplate").tmpl(data)).fadeIn("fast").toggleClass("active");
      });
  }


  function handleConfirmOrder(event) {
    window.location = "<c:url value="/payment.html"/>";
  }

  function handlePromotions() {
    $.get('/mbm/api/v1/catalog/item/search',
      function (data) {
        var results = data.results;
        for (var i = 0; i < results.length; i++) {
          $('#catalog-item-search-output').append($("#searchItemTemplate").tmpl(results[i]));
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
    $.atmosphere.request = {transport:'websocket'});

  $("button").each(function () {
    $(this).button();
  });

  $(document).ready(function () {
    // Populate the search results
    handlePromotions();

  });

</script>

</body>
</html>
