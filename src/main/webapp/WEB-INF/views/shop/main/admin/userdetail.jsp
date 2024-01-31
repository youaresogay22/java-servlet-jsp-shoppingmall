<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-01-30
  Time: 오후 3:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <title>사용자 상세정보 페이지</title>
</head>
<body>
<h1>사용자 상세 정보</h1>
<h2>사용자 ID: ${requestScope.user.getUserId()}</h2>
<h2>사용자 이름: ${requestScope.user.getUserName()}</h2>
<h2>사용자 생년월일: ${requestScope.user.getUserBirth()}</h2>
<c:choose>
    <c:when test="${requestScope.user.getUserAuth() eq 'ROLE_USER'}">
        <h2>사용자 권한: 일반 사용자</h2>
    </c:when>
    <c:when test="${requestScope.user.getUserAuth() eq 'ROLE_ADMIN'}">
        <h2>사용자 권한: 관리자</h2>
    </c:when>
</c:choose>
<h2>사용자 보유 포인트: ${requestScope.user.getUserPoint()}</h2>
<h2>사용자 가입일자: ${requestScope.user.getCreatedAt()}</h2>
<h2>사용자 최근 로그인 일자: ${requestScope.user.getLatestLoginAt()}</h2>
</body>
</html>
