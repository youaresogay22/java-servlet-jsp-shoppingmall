package com.nhnacademy.shoppingmall.controller.manage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping(method = RequestMapping.Method.GET, value = {"/deleteOrChangeProduct.do"})
public class DeleteOrChangeProductController implements BaseController {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String category = req.getParameter("category");

        return "shop/main/admin/productdeleteorchange";
    }
}