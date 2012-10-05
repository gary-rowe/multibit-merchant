// Catalog API bindings

var searchTemplate="<div class='mbm-item ui-widget-content ui-corner-all'><a href='#' class='mbm-item-link'>{title}</a><img class='mbm-item-thumbnail float-right' src='{imgThumbnailUri}'/></a><p>{summary}</p><p>3.25BTC (&euro;6.50)</p><button id='item-1' class='mbm-add-to-basket' onclick='handleAddToBasketClick(this)'>Add to basket</button></div>";

// Item create
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