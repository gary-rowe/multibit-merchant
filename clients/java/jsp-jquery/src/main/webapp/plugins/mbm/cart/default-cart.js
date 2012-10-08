function handleAddToCartClick(uri) {
  console.log("Adding item to cart");
  $.post(uri,
    function (data) {
      var cartItems = data.cartItems;
      $('#cart-contents').html("");
      for (var i = 0; i < cartItems.length; i++) {
        $('#cart-items').append($("#cartItemTemplate").tmpl(cartItems[i]));
      }
      $('#cart-total').html($("#cartTotalTemplate").tmpl(data));
    });
}
