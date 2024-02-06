package com.nhnacademy.shoppingmall.controller.manage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.execption.ProductAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.product.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(method = RequestMapping.Method.POST, value = "/productManageAction.do")
public class productManagePostController implements BaseController {
    ProductService productService = new ProductServiceImpl(new ProductRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //상품등록 구현
        //필요 변수 선언
        HashMap<String, Object> map = new HashMap<>();
        String id, modelNumber, modelName, description;
        Blob productThumbnail, productImage;
        BigDecimal unitCost;
        int unitQuantity;

        // 파일 처리를 위해 apache commons fileupload 라이브러리 사용
        DiskFileItemFactory diskFactory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(diskFactory);
        try {
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    map.put(item.getFieldName(), item.getString());
                } else
                    map.put(item.getFieldName(), item.get());
            }
        } catch (FileUploadException e) {
            throw new RuntimeException(e);
        }

        try {
            id = (String) map.get("productId");
            modelNumber = (String) map.get("modelNumber");
            //모델명: 한글 깨짐 방지 처리
            modelName = new String(((String) map.get("modelName")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            productThumbnail = new SerialBlob((byte[]) map.get("productThumbnail"));
            productImage = new SerialBlob((byte[]) map.get("productImage"));
            unitCost = new BigDecimal((String) map.get("unitCost"));
            unitQuantity = Integer.parseInt((String) map.get("unitQuantity"));
            //상품설명: 한글 깨짐 방지 처리
            description = new String(((String) map.get("description")).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            log.debug("hello save product: {},{},{}", id, modelName, description);

        } catch (SQLException | NullPointerException e) {
            throw new RuntimeException(e);
        }

        Product product = productService.getProduct("ProductID", id);

        try {
            if (product == null) {
                Product newProduct = new Product(id, modelNumber, modelName, productThumbnail, productImage, unitCost, unitQuantity, description);
                productService.saveProduct(newProduct);
                req.setAttribute("saveSucceed", true);
                return "shop/main/admin/productmanage_form";
            } else throw new ProductAlreadyExistsException(id);

        } catch (ProductAlreadyExistsException e) {
            DbConnectionThreadLocal.setSqlError(true);
            log.debug("product save execption");
            req.setAttribute("duplicateProductId", true);
            return "shop/main/admin/productmanage_form";
        }
    }
}


