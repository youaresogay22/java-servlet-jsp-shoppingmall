<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-02-06
  Time: 오전 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>IDIOT</title>
</head>
<body>
<h1>ERROR PAGE</h1>
<h1>ERROR CODE: ${requestScope.status_code}</h1>
<h1>EXCEPTION TYPE: ${requestScope.exception_type}</h1>
<h1>ERROR MESSAGE: ${requestScope.message}</h1>
<h1>EXCEPTION: ${requestScope.exception}</h1>
<h1>FROM URI: ${requestScope.request_uri}</h1>

</body>
</html>
