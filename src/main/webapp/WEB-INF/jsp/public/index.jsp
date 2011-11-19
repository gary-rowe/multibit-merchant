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

    <div class="ui-widget-content">
      <p>${greeting}</p>

      <div class="mbm-item">
        <!-- TODO make this driven by the model -->
        <a href="#" class="mbm-item-link">Cryptonomicon, By Neal Stephenson</a>
        <p></p>
        <img class="mbm-item-thumbnail" src="<c:url value="/images/catalog/items/2/thumbnail2.png" />"/>
        <p>A brilliant patchwork of codebreaking mathematicians and their descendants who are striving to create a
          datahaven in the Philippines. '...trust me on this one' Guardian</p>
        <img src="<c:url value="/images/catalog/items/2/swatch2b.png" />"
             draggable="true"
             class="borderimage"
             onMouseover="borderit(this,'#9ec0da')"
             onMouseout="borderit(this,'#e2e5e7')"
             alt="Drag to MultiBit to buy"
             title="Drag to MultiBit to buy"/>
      </div>
      <div class="mbm-item">
        <a href="#" class="mbm-item-link">A Year in Provence, By Peter Mayle</a>
        <p></p>
        <img class="mbm-item-thumbnail" src="<c:url value="/images/catalog/items/1/thumbnail1.png" />"/>

        <p>Enjoy an irresistible feast of humour and discover the joys of French rural living with Peter Mayle's
          bestselling, much-loved account of 'A Year In Provence'.</p>
        <img src="<c:url value="/images/catalog/items/1/swatch1.png" />"
             draggable="true"
             class="borderimage"
             onMouseover="borderit(this,'#9ec0da')"
             onMouseout="borderit(this,'#e2e5e7')"
             alt="Drag to MultiBit to buy"
             title="Drag to MultiBit to buy"/>
      </div>
      <div class="mbm-item">
        <a href="#" class="mbm-item-link">Plumbing and Central Heating, By Mike Lawrence</a>
        <p></p>
        <img class="mbm-item-thumbnail" src="<c:url value="/images/catalog/items/3/thumbnail3.png" />"/>

        <p>This guide begins with the basic skills of plumbing, which once mastered, can be applied to any situation,
          from mending a leaking tap to installing a new shower unit.</p>
        <img src="<c:url value="/images/catalog/items/3/swatch3.png" />"
             draggable="true"
             class="borderimage"
             onMouseover="borderit(this,'#9ec0da')"
             onMouseout="borderit(this,'#e2e5e7')"
             alt="Drag to MultiBit to buy"
             title="Drag to MultiBit to buy"/>
      </div>
      <div class="mbm-item">
        <a href="#" class="mbm-item-link">The Quantum Thief, By Hannu Rajaniemi</a>
        <p></p>
        <img class="mbm-item-thumbnail" src="<c:url value="/images/catalog/items/4/thumbnail4.png" />"/>

        <p>The most exciting SF debut of the last five years - a star to stand alongside Alistair Reynolds and Richard
          Morgan.</p>
        <img src="<c:url value="/images/catalog/items/4/swatch4.png" />"
             draggable="true"
             class="borderimage"
             onMouseover="borderit(this,'#9ec0da')"
             onMouseout="borderit(this,'#e2e5e7')"
             alt="Drag to MultiBit to buy"
             title="Drag to MultiBit to buy"/>
      </div>
      <div class="mbm-item">
        <a href="#" class="mbm-item-link">The Complete Works of Emily Dickinson, Edited by Thomas H Johnson</a>
        <p></p>
        <img class="mbm-item-thumbnail" src="<c:url value="/images/catalog/items/5/thumbnail5.png" />"/>

        <p>The Complete Poems of Emily Dickinson is the only one-volume edition containing all Emily Dickinson's
          poems.</p>
        <img src="<c:url value="/images/catalog/items/5/swatch5.png" />"
             draggable="true"
             class="borderimage"
             onMouseover="borderit(this,'#9ec0da')"
             onMouseout="borderit(this,'#e2e5e7')"
             alt="Drag to MultiBit to buy"
             title="Drag to MultiBit to buy"/>
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
    $("#alert").slideToggle("slow").toggleClass("active").delay(2000).slideToggle("slow").toggleClass("active");

  }

  /**
   * HTML5 drag event event handler
   */
  function dragStartHandler(event) {
    // TODO Interrogate the underlying item for it's Bitcoin address
    alert("Dragging " + event);
  }


  // Initialisation

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
