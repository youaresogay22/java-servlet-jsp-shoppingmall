package com.nhnacademy.shoppingmall.controller.manage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.execption.CouldNotSaveProductException;
import com.nhnacademy.shoppingmall.product.execption.ProductAlreadyExistsException;
import com.nhnacademy.shoppingmall.product.repository.impl.ProductRepositoryImpl;
import com.nhnacademy.shoppingmall.product.service.ProductService;
import com.nhnacademy.shoppingmall.product.service.impl.ProductServiceImpl;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
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

            //존재하지 않는 상품id인 경우에만 등록
            if (product == null) {
                Product newProduct = handleUploadAndReturnProduct(mReq, resourcePath);
                log.debug("{}", newProduct);
                //02.08 수정: DB에는 저장되지만 파일 업로드 안되는 경우가 있어 예외처리
                if (newProduct != null) {
                    productService.saveProduct(newProduct);
                    req.setAttribute("saveSucceed", true);
                    return "shop/main/admin/addproduct_form";

                } else throw new CouldNotSaveProductException(id);
            } else throw new ProductAlreadyExistsException(id);

        } catch (IOException | ProductAlreadyExistsException e) {
            log.debug("product Already Exists");
            req.setAttribute("duplicateProductId", true);
            return "shop/main/admin/addproduct_form";

        } catch (CouldNotSaveProductException e) {
            log.debug("product save execption");
            req.setAttribute("productSaveError", true);
            return "shop/main/admin/addproduct_form";
        }
    }
    
    private Product handleUploadAndReturnProduct(MultipartRequest mReq, String fileSavePath) {
        //필요 변수 선언
        String id, modelNumber, modelName, description;
        BigDecimal unitCost;
        int unitQuantity;
        // 02.08 수정: yyyyMMddHH 사용시 동일 시간내 업로드 시 중복 파일명 이슈로 오류 발생하여 yyyyMMddHHmmss 로 수정
        // #todo: 그냥 파일명 해쉬처리 하는게 나을 것 같지만 시간이 부족함
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        //파일 업로드+ 파일명 가져오기(cos 라이브러리상 어쩔 수 없음, MultipartRequest 생성 시 파일 즉시 업로드됨)
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
        try {
            handleFile(fileSavePath, uploaded_thumbnailName, new_thumbnailName);
            handleFile(fileSavePath, uploaded_imageName, new_imageName);
        } catch (IOException | RuntimeException e) {
            log.debug("There was File Delete Exception AGAIN");
            return null;
        }

        return new Product(id, modelNumber, modelName, new_thumbnailName, new_imageName, unitCost, unitQuantity, description);
    }

    private void handleFile(String path, String oldFileName, String newFileName) throws IOException {
        File oldFile = new File(path + oldFileName);
        File newFile = null;
        String t_path = path + "thumbnails/";
        String i_path = path + "product_images/";

        Files.createDirectories(Paths.get(t_path));
        Files.createDirectories(Paths.get(i_path));
        log.debug("oldname: {}, newname: {}", oldFileName, newFileName);

        try {
            // 썸네일 혹은 이미지에 따른 하위 폴더 지정
            switch (newFileName.charAt(0)) {
                case 'T':
                    newFile = new File(t_path + newFileName);
                    break;
                case 'I':
                    newFile = new File(i_path + newFileName);
                    break;
            }
            log.debug("newfile: {}", newFile);
            if (newFile != null) {
                FileUtils.moveFile(oldFile, newFile);
            } else throw new RuntimeException();

        } catch (IOException | RuntimeException e) {
            log.debug("There was File Move Exception");
            //handles something code
            FileUtils.delete(oldFile);
        }
    }
}


