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
        <form method="post" action="/signupAction.do">
            <c:choose>
                <c:when test="${not empty requestScope.duplicateId}">
                    <h1 class="h3 mb-3 fw-normal" style="color: red">존재하는 아이디 입니다.</h1>
                </c:when>

                <c:otherwise>
                    <h1 class="h3 mb-3 fw-normal">반갑습니다</h1>
                </c:otherwise>
            </c:choose>

            <div class="form-floating">
                <input type="text" name="user_id" class="form-control" id="user_id" placeholder="회원 아이디" required>
                <label for="user_id">회원아이디</label>
            </div>

            <div class="form-floating">
                <input type="password" name="user_password" class="form-control" id="user_password" placeholder="비밀번호"
                       required>
                <label for="user_password">비밀번호</label>
            </div>

            <div class="form-floating">
                <input type="text" name="user_name" class="form-control" id="user_name" placeholder="회원이름"
                       required>
                <label for="user_name">이름</label>
            </div>

            <div class="form-floating">
                <input type="text" name="user_birth" class="form-control" id="user_birth"
                       placeholder="예)19901024" required>
                <label for="user_birth">생년월일 예)19901024</label>
            </div>

            <button class="w-100 btn btn-lg btn-primary mt-3" type="submit">가입하기</button>

            <p class="mt-5 mb-3 text-muted">© 2022-2024</p>

        </form>
    </div>
</div>