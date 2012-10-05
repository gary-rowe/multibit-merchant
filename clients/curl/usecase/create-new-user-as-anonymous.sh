# Create a new User as an anonymous visitor
curl -v \
--cookie cookies.txt --cookie-jar cookies.txt \
-H "Accept: application/json" \
-H "Content-Type: application/json" \
-X POST \
-d '{"sessionId":"", "username": "test", "password" : "test1", "oneTimeUse" : "false"}' \
"http://localhost:8080/mbm/api/v1/user"