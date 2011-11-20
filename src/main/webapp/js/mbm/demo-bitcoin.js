// Bitcoin API bindings
$("#bitcoin-monitor").bind("click", function(event) {

  $.post('/mbm/api/v1/bitcoin/monitor/1abcdefgh',
    function(data) {
      $('#bitcoin-monitor-output').html(data);
    });
});
