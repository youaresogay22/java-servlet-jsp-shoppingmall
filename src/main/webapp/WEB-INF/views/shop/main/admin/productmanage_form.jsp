<%@ page contentType="text/html;charset=UTF-8" language="java" trimDirectiveWhitespaces="true" session="false" %>

<div style="margin: auto; width: 600px;">
    <div class="p-2">
        <form method="post" action="/productManage.do" enctype="multipart/form-data">

            <h1 class="h3 mb-3 fw-normal">상품 등록하기</h1>

            <div class="form-floating">
                <input type="text" name="product_id" class="form-control" id="product_id" placeholder="회원 상품ID"
                       required>
                <label for="product_id">상품ID</label>
            </div>

            <div class="form-floating">
                <input type="text" name="product_password" class="form-control" id="product_password" placeholder="상품번호"
                       required>
                <label for="product_password">상품번호</label>
            </div>

            <div class="form-floating">
                <input type="text" name="product_name" class="form-control" id="product_name" placeholder="상품이름"
                       required>
                <label for="product_name">상품이름</label>
            </div>

            <div class="form-floating">
                <input type="file" name="product_thumbnail" class="form-control" id="product_thumbnail"
                       placeholder="썸네일 이미지" required accept="image/*">
                <label for="product_thumbnail">썸네일 이미지</label>
            </div>

            <div class="form-floating">
                <input type="file" name="product_thumbnail" class="form-control" id="product_image"
                       placeholder="이미지" required accept="image/*">
                <label for="product_thumbnail">상품 이미지</label>
            </div>

            <div class="form-floating">
                <input type="text" name="product_cost" class="form-control" id="product_cost" placeholder="상품가격"
                       required>
                <label for="product_name">상품가격</label>
            </div>

            <div class="form-floating">
                <input type="text" name="product_description" class="form-control" id="product_description"
                       placeholder="상품설명" required>
                <label for="product_description">상품설명</label>
            </div>
            <button class="w-100 btn btn-lg btn-primary mt-3" type="submit">등록하기</button>

            <p class="mt-5 mb-3 text-muted">© 2022-2024</p>

        </form>
    </div>
</div>