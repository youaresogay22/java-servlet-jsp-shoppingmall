<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" session="false" %>

<div style="margin: auto; width: 600px;">
    <div class="p-2">
        <form method="post" action="/addProductAction.do" enctype="multipart/form-data" accept-charset="UTF-8">
            <c:choose>
                <c:when test="${not empty requestScope.duplicateProductId}">
                    <h1 class="h3 mb-3 fw-normal" style="color: red">존재하는 상품 id 입니다.</h1>
                </c:when>

                <c:when test="${not empty requestScope.productSaveError}">
                    <h1 class="h3 mb-3 fw-normal" style="color: red">파일 업로드 중 오류가 발생했습니다.</h1>
                </c:when>

                <c:when test="${not empty requestScope.saveSucceed}">
                    <h1 class="h3 mb-3 fw-normal" style="color: blue">등록하였습니다.</h1>
                </c:when>

                <c:otherwise>
                    <h1 class="h3 mb-3 fw-normal">상품 등록하기</h1>
                </c:otherwise>
            </c:choose>

            <div class="form-floating">
                <input type="text" name="productId" class="form-control" id="productId" placeholder="상품ID"
                       required>
                <label for="productId">상품ID</label>
            </div>

            <div class="form-floating">
                <input type="text" name="modelNumber" class="form-control" id="modelNumber" placeholder="상품번호"
                       required>
                <label for="modelNumber">상품번호</label>
            </div>

            <div class="form-floating">
                <input type="text" name="modelName" class="form-control" id="modelName" placeholder="상품이름"
                       required>
                <label for="modelName">상품이름</label>
            </div>

            <div class="form-floating">
                <input type="file" name="productThumbnail" class="form-control" id="productThumbnail"
                       placeholder="썸네일 이미지" required accept="image/*">
                <label for="productThumbnail">썸네일 이미지</label>
            </div>

            <div class="form-floating">
                <input type="file" name="productImage" class="form-control" id="productImage"
                       placeholder="이미지" required accept="image/*">
                <label for="productImage">상품 이미지</label>
            </div>

            <div class="form-floating">
                <input type="text" name="unitCost" class="form-control" id="unitCost" placeholder="상품가격"
                       required>
                <label for="unitCost">상품가격</label>
            </div>
            <div class="form-floating">
                <input type="text" name="unitQuantity" class="form-control" id="unitQuantity"
                       placeholder="상품재고" required>
                <label for="unitQuantity">상품재고</label>
            </div>
            <div class="form-floating">
                <input type="text" name="description" class="form-control" id="description"
                       placeholder="상품설명" required>
                <label for="description">상품설명</label>
            </div>
            <button class="w-100 btn btn-lg btn-primary mt-3" type="submit">등록하기</button>

            <p class="mt-5 mb-3 text-muted">© 2022-2024</p>

        </form>
    </div>
</div>