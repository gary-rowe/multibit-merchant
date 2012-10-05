# Retrieve all items
curl -v --cookie cookies.txt --cookie-jar cookies.txt -H "Accept: application/json" "http://localhost:8080/mbm/api/v1/items"