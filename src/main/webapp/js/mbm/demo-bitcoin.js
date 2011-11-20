// Bitcoin API bindings
$("#bitcoin-new-address").bind("click", function(event) {
  $.post('/mbm/api/v1/bitcoin/new-address',
    function(data) {
      $('#bitcoin-new-address-output').html(data);
    });
});
