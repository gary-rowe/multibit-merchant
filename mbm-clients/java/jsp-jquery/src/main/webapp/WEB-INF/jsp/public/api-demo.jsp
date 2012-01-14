<%@include file="/WEB-INF/jspf/public/taglibs.jspf" %>
<!DOCTYPE html>
<html>
<head>
  <%@include file="/WEB-INF/jspf/public/head.jspf" %>
</head>
<body>
<div id="switcher"></div>
<div><a href="<c:url value="/index.html" />">Home</a> | <a href="<c:url value="/admin.html" />">Admin</a></div>
<div id="tabs">
  <ul>
    <li><a href="#tab-1">Alerts</a></li>
    <li><a href="#tab-2">Time</a></li>
    <li><a href="#tab-3">Bitcoin</a></li>
    <li><a href="#tab-4">Catalog</a></li>
    <li><a href="#tab-5">Cart</a></li>
  </ul>

  <div id="tab-1">
    <p>The Alert API provides notification text that a customer should pay attention to.</p>
    <table>
      <colgroup>
        <col span="1" style="width:10%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:50%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a href="http://localhost:8080/mbm/api/v1/alert/subscribe">Subscribe to 'alert'</a></td>
        <td>TODO require demo</td>
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
        <col span="1" style="width:20%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:50%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a href="http://localhost:8080/mbm/api/v1/time/now">Now UTC</a></td>
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
  <div id="tab-3">
    <p>The Bitcoin API provides access to various Bitcoin related facilities.</p>
    <table>
      <colgroup>
        <col span="1" style="width:10%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:50%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a id="bitcoin-new-address" href="#">Request new address</a></td>
        <td>Request a Bitcoin address from the MBM pool. Will respond with an address if successful.</td>
        <td><code>$("#bitcoin-new-address").bind("click", function(event) {
          $.post('/mbm/api/v1/bitcoin/new-address',
          function(data) {
          $('#bitcoin-new-address-output').html(data);
          });
          });
        </code>
        </td>
        <td id="bitcoin-new-address-output"></td>
      </tr>
      <tr>
        <td><a id="bitcoin-swatch" href="#">Swatch</a></td>
        <td>Create a Bitcoin swatch.
        </td>
        <td><code></code>
        </td>
        <td id="bitcoin-swatch-output"><img
          src="<c:url value="/api/v1/bitcoin/swatch?address=1abcdefg&amount=4.5&label=Example"/>"/></td>
      </tr>
    </table>

    <p>
    </p>
  </div>

  <div id="tab-4">
    <p>The Catalog API provides access to various catalog related facilities (items, file uploads etc).</p>
    <table>
      <colgroup>
        <col span="1" style="width:10%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:50%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a id="cart-add" href="#">Add to cart</a></td>
        <td>Add 2 items to the cart (token='df575838-94dd-4b6b-8c86-8fc2371bd883').</td>
        <td><code>$("#cart-add").bind("click", function(event) {
          $.post('/mbm/api/v1/cart',
          function(data) {
            var cartItems = data.cartItemSummaries;
            $('#cart-items').html("");
            for (var i = 0; i < cartItems.length; i++) {
            $('#cart-items').append($("#cartItemTemplate").tmpl(cartItems[i]));
            }
            $('#cart-total').html($("#cartTotalTemplate").tmpl(data));
          });
        </code>
        </td>
        <td id="cart-add-output"></td>
      </tr>
    </table>

    <p>
    </p>
  </div>

  <div id="tab-5">
    <p>The Cart API provides access to various shopping cart related facilities (etc).</p>
    <table>
      <colgroup>
        <col span="1" style="width:10%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:20%"/>
        <col span="1" style="width:50%"/>
      </colgroup>
      <tr>
        <td>Example</td>
        <td>Description</td>
        <td>Code</td>
        <td>Output</td>
      </tr>
      <tr>
        <td><a id="catalog-item-create" href="#">Request new item</a></td>
        <td>Request a new Item. Will respond with a .</td>
        <td><code>$("#catalog-item-create").bind("click", function(event) {
          $.post('/mbm/api/v1/catalog/item',
          function(data) {
          $('#catalog-item-create-output').html(data);
          });
          });
        </code>
        </td>
        <td id="catalog-item-create-output"></td>
      </tr>
      <tr>
        <td><a id="catalog-item-search" href="#">Search item</a></td>
        <td>Search for a catalog item.
        </td>
        <td><code>$("#catalog-item-search").bind("click", function(event) {
          $.get('/mbm/api/v1/catalog/item/search?q=0099410672',
          function(data) {
          $('#catalog-item-search-output').html(data);
          });
          });
        </code>
        </td>
        <td id="catalog-item-search-output"></td>
      </tr>
    </table>

    <p>
    </p>
  </div>


</div>

<%@include file="/WEB-INF/jspf/public/cdn-scripts.jspf" %>
<%@include file="/WEB-INF/jspf/public/mbm-scripts.jspf" %>

<script type="text/javascript" src="<c:url value="/js/mbm/demo/demo-time.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/mbm/demo/demo-bitcoin.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/mbm/demo/demo-catalog.js"/>"></script>
</body>
</html>
