package com.nhnacademy.shoppingmall.controller.manage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequestMapping(method = RequestMapping.Method.POST, value = "/userManage.do")
public class productManage implements BaseController {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        return null;
    }
}

