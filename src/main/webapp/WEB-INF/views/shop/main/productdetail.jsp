<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-02-06
  Time: 오후 2:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <title>상품 상세정보 페이지</title>
</head>
<body>
<h1>상품 상세 정보</h1>
<h2>상품 ID: ${requestScope.product.getProductId()}</h2>
<h2>상품 번호: ${requestScope.product.getModelNumber()}</h2>
<h2>상품 이름: ${requestScope.product.getModelName()}</h2>
<h2>상품 상세 이미지</h2>
<img src="../${pageContext.request.contextPath}/resources/product_images/${requestScope.product.getProductImage()}">
<h2>상품 가격: ${requestScope.product.getUnitCost()}</h2>
<h2>상품 재고: ${requestScope.product.getUnitQuantity()}</h2>
<h2>상품 상세설명:</h2>
<p> ${requestScope.product.getDescription()}</p>
</body>
</html>