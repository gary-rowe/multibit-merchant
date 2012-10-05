// Time API bindings
$("#json-time").bind("click", function(event) {
  $.getJSON(
    "http://localhost:8080/mbm/api/v1/time/extended",
    null,
    function (data) {
      $("#json-time-output").html(
        + data.year + "-"
        + data.month + "-"
        + data.day + " "
        + data.hour + ":"
        + data.minute + ":"
        + data.second
      );
    })
});
