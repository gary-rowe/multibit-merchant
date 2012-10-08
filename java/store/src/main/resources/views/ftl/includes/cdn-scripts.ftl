<!-- All scripts at the end of the page for faster presentation -->
<!-- 1) jQuery CDN - use Google for fastest HTTPS support over Europe/USA -->
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<!-- 2) Bootstrap CDN -->
<script src="//netdna.bootstrapcdn.com/twitter-bootstrap/2.1.1/js/bootstrap.min.js"></script>

<script>
  $(function () {
    $('#theme_switcher ul li a').bind('click',
      function (e) {
        $("#switch_style").attr("href", "http://bootswatch.com/" + $(this).attr('rel') + "/bootstrap.min.css");
        return false;
      }
    );
  });
</script>