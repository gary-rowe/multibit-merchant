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
  </div>
</script>

<script id="itemDetailTemplate" type="text/x-jquery-tmpl">
  <div class='mbm-item ui-widget-content ui-corner-all'>
    <a href='#' onclick='handleAddToBasketClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")' class='mbm-item-link'>\${title}</a>
    <a href="#" onclick='handleAddToBasketClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")'><img
      class='mbm-item-thumbnail float-right' src='\${imgThumbnailUri}'/></a>

    <p>Get it by <strong>\${offeredDeliveryDate}</strong> if you order it today.</p>

    <p><strong>\${btcPrice} BTC</strong> (\${localSymbol} \${localPrice})</p>

    <button id='\${id}' onclick='handleAddToBasketClick("/mbm/api/vi/cart/\${id}")'>Add to cart</button>

  </div>
</script>

<script id="cartDetailTemplate" type="text/x-jquery-tmpl">
  <div class='mbm-item ui-widget-content ui-corner-all'>
    <a href='#' onclick='handleItemDetailClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")' class='mbm-item-link'>\${title}</a>
    <a href="#" onclick='handleItemDetailClick("/mbm/api/v1/catalog/item/\${id}/\${slug}")'><img
      class='mbm-item-thumbnail float-right' src='\${imgThumbnailUri}'/></a>

    <p>x 1 @ \${btcPrice} BTC (\${localSymbol} \${localPrice})</p>

    <p><strong>\${btcPrice} BTC</strong> (\${localSymbol} \${localPrice})</p>

    <p><input type="hidden" name="action" value="add_to_basket"></p>

    <p><input type="hidden" name="addquant" value="1">
      <input type="hidden" name="item" value="sku10364">
      <input type="hidden" id="m_dfd0e8f6f22250c2bdf14a0125977fdb_selected_item" name="selected_item" value="10364">
				<span class="skuname">
White				</span></p>

    <table width="100%" class="no_cell_padding">
      <tbody>
      <tr>
        <td class="price">
          £149.99
        </td>
        <td>
          <img src="http://media.firebox.com/i/spinner.gif" style="display: none;" class="buy_button_spinner"
               id="m_e0dce987ce9a25058e072168f85d3efb_spinner" width="16" height="16" alt="spin, spin">
        </td>
        <td rowspan="1">
          <div style="float: right; text-align: center;">
            <input type="submit" id="butt_f4568302afd5fd1b4a65ee0fbda9f099" name="butt_f4568302afd5fd1b4a65ee0fbda9f099"
                   onmouseover="this.className='freshbutton_base freshbutton_green_over';"
                   onmousedown="this.className='freshbutton_base freshbutton_green_down';"
                   onmouseup="this.className='freshbutton_base freshbutton_green_over';"
                   onmouseout="this.className='freshbutton_base freshbutton_green_up';"
                   class="freshbutton_base freshbutton_green_up" value="Buy"
                   onclick="return addbuttons_ajax_submission('submit_basket_form(\'m_dfd0e8f6f22250c2bdf14a0125977fdb\', \'m_e0dce987ce9a25058e072168f85d3efb_spinner\' )')"
                   style=""></div>
        </td>
      </tr>
      <tr>
        <td colspan="2" valign="top">
          <div class="price" style="font-size: 1.2em; position: relative; top: -0.25em">with free delivery</div>
        </td>
        <td class="add_to_wishlist" style="white-space: nowrap" valign="top">
          <a href="http://www.firebox.com/product/3731/Skatecycle?wl_add"
             onclick="return add_to_wishlist('m_dfd0e8f6f22250c2bdf14a0125977fdb', 'm_e0dce987ce9a25058e072168f85d3efb_spinner', '');">Add
            to wishlist</a></td>
      </tr>
      </tbody>
    </table>
  </div>
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

  function handleAddToCartClick(event) {
    console.log("Adding item to cart");
    $.post(uri,
      function (data) {
        var results = data.results;
        for (var i = 0; i < results.length; i++) {
          $('#cart-detail').append($("#cartDetailTemplate").tmpl(results[i]));
        }
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
