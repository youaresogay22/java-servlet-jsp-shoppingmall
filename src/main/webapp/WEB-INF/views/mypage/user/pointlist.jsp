<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-02-02
  Time: 오후 5:03
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="ko">
<head>
    <title>포인트 사용내역 관리 페이지</title>
</head>
<body>
<div class="container">
    ${requestScope.userId} 님의 포인트 사용 이력
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
            <th scope="col">사용/적립 내역 번호</th>
            <th scope="col">변동 사유</th>
            <th scope="col">사용/적립 양</th>
            <th scope="col">사용/적립 일자</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty requestScope.list}">
                <c:forEach var="item" items="${requestScope.list}" varStatus="status">
                    <tr>
                        <td>${status.count}</td>
                        <td>${item.getRequestId()}</td>
                        <td>${item.getChangeinfo().toString()}</td>
                        <td>${item.getChangeAmount()}</td>
                        <td>${item.getChangeDate()}</td>
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
                <c:when test="${requestScope.startpageno eq 1}">
                    <li class="page-item disabled">
                        <a class="page-link" href="#" tabindex="-1" aria-disabled="true">이전</a>
                    </li>
                </c:when>

                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link" href="/managePointDetail.do?page_temp=${requestScope.startpageno -1}"
                           tabindex="-1"
                           aria-disabled="true">이전</a>
                    </li>
                </c:otherwise>
            </c:choose>

            <c:forEach var="i" begin="${requestScope.startpageno}" end="${requestScope.endpageno}"
                       step="1">
                <li class="page-item">
                    <a class="page-link digit" href="/managePointDetail.do?page_temp=${i}">${i}</a>
                </li>
            </c:forEach>

            <c:choose>
                <c:when test="${requestScope.totalpages eq requestScope.endpageno}">
                    <li class="page-item disabled"><a class="page-link" href="#">다음</a></li>
                </c:when>

                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="/managePointDetail.do?page_temp=${requestScope.endpageno+1}">다음</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</div>

<%--<script src="http://code.jquery.com/jquery-latest.js"></script>--%>
<%--<script src="../resources/userlist_js.js"></script>--%>
</body>
</html>
