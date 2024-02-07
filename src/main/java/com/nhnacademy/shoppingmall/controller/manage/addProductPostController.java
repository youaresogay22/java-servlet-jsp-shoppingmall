package com.nhnacademy.shoppingmall.controller.manage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.execption.ProductAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.product.service.impl.ProductServiceImpl;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequestMapping(method = RequestMapping.Method.POST, value = "/addProductAction.do")
public class addProductPostController implements BaseController {
    ProductService productService = new ProductServiceImpl(new ProductRepositoryImpl());
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        final String resourcePath = req.getServletContext().getRealPath("resources/");

        try {
            MultipartRequest mReq = new MultipartRequest(req, resourcePath, 15 * 1024 * 1024, "UTF-8", new DefaultFileRenamePolicy());
            //존재하는 상품 id 인지 검사
            String id = mReq.getParameter("productId");
            Product product = productService.getProduct("ID", id);
            log.debug("findproduct query result: {}", product);

            if (product == null) {
                Product newProduct = handleUploadAndReturnProduct(mReq, resourcePath);
                productService.saveProduct(newProduct);
                req.setAttribute("saveSucceed", true);
                return "shop/main/admin/addproduct_form";
            } else throw new ProductAlreadyExistsException(id);

        } catch (IOException | ProductAlreadyExistsException e) {
            log.debug("product save execption");
            req.setAttribute("duplicateProductId", true);
            return "shop/main/admin/addproduct_form";
        }
    }
    
    private Product handleUploadAndReturnProduct(MultipartRequest mReq, String fileSavePath) {
        //필요 변수 선언
        String id, modelNumber, modelName, description;
        BigDecimal unitCost;
        int unitQuantity;
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));

        //파일 업로드+ 파일명 가져오기(cos 라이브러리상 어쩔 수 없음)
        String uploaded_thumbnailName = mReq.getFilesystemName("productThumbnail");
        String uploaded_imageName = mReq.getFilesystemName("productImage");

        //신규 파일명 생성 후 assign(ex:(썸네일 T, 이미지 I)T_88848_240207_rac.확장자)
        String new_thumbnailName = "T_" + now + "_" + StringUtils.left(uploaded_thumbnailName, 3) + "." + StringUtils.substringAfter(uploaded_thumbnailName, ".");
        String new_imageName = "I_" + now + "_" + StringUtils.left(uploaded_imageName, 3) + "." + StringUtils.substringAfter(uploaded_imageName, ".");

        // 변수 assign
        id = mReq.getParameter("productId");
        modelName = mReq.getParameter("modelName");
        modelNumber = mReq.getParameter("modelNumber");
        description = mReq.getParameter("description");
        unitCost = new BigDecimal(mReq.getParameter("unitCost"));
        unitQuantity = Integer.parseInt(mReq.getParameter("unitQuantity"));

        //파일 및 디렉토리 이동 처리
        handleFile(fileSavePath, uploaded_thumbnailName, new_thumbnailName);
        handleFile(fileSavePath, uploaded_imageName, new_imageName);

        return new Product(id, modelNumber, modelName, new_thumbnailName, new_imageName, unitCost, unitQuantity, description);
    }

    private void handleFile(String path, String oldFileName, String newFileName) {
        try {
            File oldFile = new File(path + oldFileName);
            File newFile = null;
            String newpath;
            // 썸네일 혹은 이미지에 따른 하위 폴더 지정
            switch (newFileName.charAt(0)) {
                case 'T':
                    newpath = path + "thumbnails/";
                    Files.createDirectories(Paths.get(newpath));
                    newFile = new File(newpath + newFileName);
                    break;
                case 'I':
                    newpath = path + "product_images/";
                    Files.createDirectories(Paths.get(newpath));
                    newFile = new File(newpath + newFileName);
                    break;
            }
            if (newFile != null) {
                FileUtils.moveFile(oldFile, newFile);
                FileUtils.delete(oldFile);
            } else throw new RuntimeException();

        } catch (IOException | RuntimeException e) {
            log.info("There was File Something Exception");
            //handles something code
        }
    }
}


