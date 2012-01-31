# Add Item to cart as Alice
curl -v \
--cookie cookies.txt --cookie-jar cookies.txt \
--user alice:alice1 \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-X POST \
-d '{"sessionId":"alice123", "cartItemSummaries": [{"id" : "1"}]}' \
"http://localhost:8080/mbm/api/v1/cart"