# Add Item to cart as Anonymous (expect failure)
curl -v \
--cookie cookies.txt --cookie-jar cookies.txt \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-X POST \
-d '{"sessionId":"", "cartItems": [{"id" : "1"}]}' \
"http://localhost:8080/mbm/api/v1/cart"