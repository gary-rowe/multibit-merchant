$(document).ready(function() {
  $("#btn-alert").click(function(){
      $("#alert").slideToggle("slow");
      $(this).toggleClass("active");
    });

});
