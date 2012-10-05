// Cart API bindings

// Add item to Cart
$("#catalog-item-create").bind("click", function(event) {
  $.post('/mbm/api/v1/catalog/item',
    function(data) {
      $('#catalog-item-create-output').html(data);
    });
});

// Item search
$("#catalog-item-search").bind("click", function(event) {
  $.get('/mbm/api/v1/catalog/item/search?q=0099410672',
    function(data) {
      var result = data.results[0];
      var html = searchTemplate.supplant(result);
      $('#catalog-item-search-output').html(html);
    });
});