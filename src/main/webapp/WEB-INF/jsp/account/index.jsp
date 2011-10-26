<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
  <title>MultiBit Merchant</title>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<p>Welcome to the account page, ${emailAddress}!</p>
<p><a href="<c:url value="/mbm/j_spring_security_logout" />" > Logout</a></p>
</body>

</html>