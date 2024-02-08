<%--
  Created by IntelliJ IDEA.
  User: nhn
  Date: 2023/11/08
  Time: 10:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ko">
<head>
    <title>포인트 사용내역 관리 페이지</title>
</head>
<body>
<div style="margin-bottom: 2%">
    <label for="categorySelector">카테고리 선택:</label>
    <select name="category" id="categorySelector">
        <option value="default" selected>전체</option>
    </select>
</div>

<div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">
    <c:choose>
        <c:when test="${not empty requestScope.list}">
            <c:forEach var="item" items="${requestScope.list}">
                <div class="col">
                    <div class="card shadow-sm">
                        <c:choose>
                            <c:when test="${not empty item.getProductThumbNail()}">
                                <img src="../${pageContext.request.contextPath}/resources/thumbnails/${item.getProductThumbNail()}"
                                     alt="${item.getProductThumbNail()}" width="100%" height="225">
                            </c:when>
                            <c:otherwise>
                                <img src="../${pageContext.request.contextPath}/resources/no-image.png"
                                     alt="no image" width="100%" height="225">
                            </c:otherwise>
                        </c:choose>
                            <%--                        <svg class="bd-placeholder-img card-img-top" width="100%" height="225"--%>
                            <%--                             xmlns="http://www.w3.org/2000/svg" role="img" aria-label="Placeholder: Thumbnail"--%>
                            <%--                             preserveAspectRatio="xMidYMid slice" focusable="false">--%>
                            <%--                            <title>Placeholder</title>--%>
                            <%--                            <rect width="100%" height="100%" fill="#55595c"></rect>--%>
                            <%--                            <text x="50%" y="50%" fill="#eceeef" dy=".3em">Thumbnail</text>--%>
                            <%--                        </svg>--%>
                        <div class="card-body">
                            <p class="card-text">${item.getModelName()}</p>
                            <p class="card-text">${item.getUnitCost()}원</p>
                            <div class="d-flex justify-content-between align-items-center">
                                <div class="btn-group">
                                    <button onclick="location.href='productDetail.do'" type="button"
                                            class="btn btn-sm btn-outline-secondary">상세보기
                                    </button>
                                </div>
                                <small class="text-muted">${item.getUnitQuantity()}개 남음</small>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p>데이터가 존재하지 않습니다.</p>
        </c:otherwise>
    </c:choose>
</div>

<div style="margin-top: 4%">
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
                        <a class="page-link" href="/index.do?page_temp=${requestScope.startpageno -1}"
                           tabindex="-1"
                           aria-disabled="true">이전</a>
                    </li>
                </c:otherwise>
            </c:choose>

            <c:forEach var="i" begin="${requestScope.startpageno}" end="${requestScope.endpageno}"
                       step="1">
                <li class="page-item">
                    <a class="page-link digit" href="/index.do?page_temp=${i}">${i}</a>
                </li>
            </c:forEach>

            <c:choose>
                <c:when test="${requestScope.totalpages eq requestScope.endpageno}">
                    <li class="page-item disabled"><a class="page-link" href="#">다음</a></li>
                </c:when>

                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="/index.do?page_temp=${requestScope.endpageno+1}">다음</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </nav>
</div>
</body>

</html>
