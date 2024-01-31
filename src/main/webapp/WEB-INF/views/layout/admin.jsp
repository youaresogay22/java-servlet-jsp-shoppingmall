<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-01-26
  Time: 오전 10:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.mysql.cj.Session" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!doctype html>
<html lang="ko">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM"
            crossorigin="anonymous"></script>
    <title>nhn ShoppingMall 관리자 페이지</title>

</head>
<body>
<div class="mainContainer">
    <header class="p-3 bg-dark text-white">
        <div class="container">
            <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">

                <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                    <svg class="bi me-2" width="40" height="32" role="img" aria-label="Bootstrap">
                        <use xlink:href="#bootstrap"></use>
                    </svg>
                </a>

                <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                    <li><a class="nav-link px-2 text-secondary" href="/index.do">Home</a></li>
                    <li><a class="nav-link px-2 text-white" href="/mypage.do">마이페이지</a></li>
                    <li><a class="nav-link px-2 text-white" href="/categoryManage.do">카테고리 관리</a></li>
                    <li><a class="nav-link px-2 text-white" href="/productManage.do">상품 관리</a></li>
                    <li><a class="nav-link px-2 text-white" href="/userManage.do">회원 관리</a></li>
                </ul>

                <form class="col-12 col-lg-auto mb-3 mb-lg-0 me-lg-3">
                    <input type="search" class="form-control form-control-dark" placeholder="Search..."
                           aria-label="Search">
                </form>

                <div class="text-end">
                    <c:choose>
                        <c:when test="${not empty sessionScope.loggedInAsUserId}">
                            <form style=display:inline-block;" method="post" action="/logout.do">
                                <button class="btn btn-outline-light me-2">
                                    로그아웃
                                </button>
                            </form>
                            <a class="btn btn-warning" href="signout.do">회원탈퇴</a>
                        </c:when>

                        <c:otherwise>
                            <a class="btn btn-outline-light me-2" href="/login.do">로그인</a>
                            <a class="btn btn-warning" href="signup.do">회원가입</a>
                        </c:otherwise>
                    </c:choose>
                </div>

            </div>
        </div>
    </header>

    <main>
        <div class="album py-5 bg-light">
            <div class="container">
                <jsp:include page="${layout_content_holder}"/>
            </div>
        </div>

    </main>

    <footer class="text-muted py-5">
        <div class="container">
            <p class="float-end mb-1">
                <a href="#">Back to top</a>
            </p>
            <p class="mb-1">shoppingmall example is © nhnacademy.com</p>
        </div>
    </footer>

</div>

</body>
</html>