<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-01-29
  Time: 오후 2:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="true" %>
<html lang="ko">
<head>
    <link rel="stylesheet" type="text/css" href="../resources/userlist_css.css">
    <title>사용자 관리 페이지</title>
</head>
<body>
<div class="container">
    <ul class="tabs">
        <li class="tab-link current" data-tab="tab-admin">관리자</li>
        <li class="tab-link" data-tab="tab-user">사용자</li>
    </ul>

    <div id="tab-admin" class="tab-content current">
        <table class="table">
            <colgroup>
                <col style="width: 10%">
                <col style="width: 20%">
                <col style="width: 20%">
                <col style="width: 20%">
                <col style="width: 20%">
                <col style="width: 10%">
            </colgroup>
            <thead class="thead-light">
            <tr>
                <th scope="col">#</th>
                <th scope="col">사용자 ID</th>
                <th scope="col">사용자 이름</th>
                <th scope="col">권한</th>
                <th scope="col">가입일자</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty requestScope.list_admin}">
                    <c:forEach var="item" items="${requestScope.list_admin}" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>${item.getUserId()}</td>
                            <td>${item.getUserName()}</td>
                            <c:choose>
                                <c:when test="${item.getUserAuth() eq 'ROLE_USER'}">
                                    <td>사용자</td>
                                </c:when>
                                <c:when test="${item.getUserAuth() eq 'ROLE_ADMIN'}">
                                    <td>관리자</td>
                                </c:when>
                            </c:choose>

                            <td>${item.getCreatedAt()}</td>
                            <td>
                                <a href="/userManageDetail.do?id=${item.getUserId()}">상세보기</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <tr>
                        <td colspan="5">데이터가 존재하지 않습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <c:choose>
                    <c:when test="${requestScope.startpageno_admin eq 1}">
                        <li class="page-item disabled">
                            <a class="page-link" href="#" tabindex="-1" aria-disabled="true">이전</a>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link" href="/userManage.do?page_admin=${requestScope.startpageno_admin -1}"
                               tabindex="-1"
                               aria-disabled="true">이전</a>
                        </li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="${requestScope.startpageno_admin}" end="${requestScope.endpageno_admin}"
                           step="1">
                    <li class="page-item">
                        <a class="page-link digit" href="/userManage.do?page_admin=${i}">${i}</a>
                    </li>
                </c:forEach>

                <c:choose>
                    <c:when test="${requestScope.totalpages_admin eq requestScope.endpageno_admin}">
                        <li class="page-item disabled"><a class="page-link" href="#">다음</a></li>
                    </c:when>

                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link"
                               href="/userManage.do?page_admin=${requestScope.endpageno_admin+1}">다음</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>
    <div id="tab-user" class="tab-content">
        <table class="table">
            <colgroup>
                <col style="width: 10%">
                <col style="width: 20%">
                <col style="width: 20%">
                <col style="width: 20%">
                <col style="width: 20%">
                <col style="width: 10%">
            </colgroup>
            <thead class="thead-light">
            <tr>
                <th scope="col">#</th>
                <th scope="col">사용자 ID</th>
                <th scope="col">사용자 이름</th>
                <th scope="col">권한</th>
                <th scope="col">가입일자</th>
                <th scope="col"></th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty requestScope.list_user}">
                    <c:forEach var="item" items="${requestScope.list_user}" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>${item.getUserId()}</td>
                            <td>${item.getUserName()}</td>
                            <c:choose>
                                <c:when test="${item.getUserAuth() eq 'ROLE_USER'}">
                                    <td>사용자</td>
                                </c:when>
                                <c:when test="${item.getUserAuth() eq 'ROLE_ADMIN'}">
                                    <td>관리자</td>
                                </c:when>
                            </c:choose>

                            <td>${item.getCreatedAt()}</td>
                            <td>
                                <a href="/userManageDetail.do?id=${item.getUserId()}">상세보기</a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <tr>
                        <td colspan="5">데이터가 존재하지 않습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                <c:choose>
                    <c:when test="${requestScope.startpageno_user eq 1}">
                        <li class="page-item disabled">
                            <a class="page-link" href="#" tabindex="-1" aria-disabled="true">이전</a>
                        </li>
                    </c:when>

                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link"
                               href="/userManage.do?page_user=${requestScope.startpageno_user -1}"
                               tabindex="-1"
                               aria-disabled="true">이전</a>
                        </li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="${requestScope.startpageno_user}" end="${requestScope.endpageno_user}"
                           step="1">
                    <li class="page-item">
                        <a class="page-link digit" href="/userManage.do?page_user=${i}">${i}</a>
                    </li>
                </c:forEach>

                <c:choose>
                    <c:when test="${requestScope.totalpages_user eq requestScope.endpageno_user}">
                        <li class="page-item disabled"><a class="page-link" href="#">다음</a></li>
                    </c:when>

                    <c:otherwise>
                        <li class="page-item">
                            <a class="page-link"
                               href="/userManage.do?page_user=${requestScope.endpageno_user+1}">다음</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </nav>
    </div>
</div>

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="../resources/userlist_js.js"></script>
</body>
</html>
