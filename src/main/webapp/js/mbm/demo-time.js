// Time API bindings
$("#json-time").click($.getJSON(
  "http://localhost:8080/mbm/api/v1/time/extended",
  null,
  function (data) {
    $("#json-time-output").html("yyyy-MM-dd="+data.year+"-"+data.month+"-"+data.day);
  })
);
