<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: UuLaptop
  Date: 2024-01-26
  Time: 오후 3:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" session="false" %>

<div style="margin: auto; width: 400px;">
    <div class="p-2">
        <form method="post" action="/manageProfileAction.do">
            <c:choose>
                <c:when test="${not empty requestScope.noPassword}">
                    <h1 class="h3 mb-3 fw-normal" style="color: red">비밀번호는 필수 입력 사항입니다.</h1>
                </c:when>

                <c:otherwise>
                    <h1 class="h3 mb-3 fw-normal">안녕하세요 ${requestScope.loggedInAsUserId} 님</h1>
                    <h1 class="h3 mb-3 fw-normal">회원 정보 수정 페이지</h1>
                </c:otherwise>
            </c:choose>

            <div class="form-floating">
                <input type="password" name="user_password" class="form-control" id="user_password" placeholder="비밀번호"
                       required>
                <label for="user_password">비밀번호</label>
            </div>

            <div class="form-floating">
                <input type="text" name="user_name" class="form-control" id="user_name" placeholder="회원이름">
                <label for="user_name">이름</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary mt-3" type="submit">수정하기</button>

            <p class="mt-5 mb-3 text-muted">© 2022-2024</p>

        </form>
    </div>
</div>