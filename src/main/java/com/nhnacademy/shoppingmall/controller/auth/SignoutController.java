package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(method = RequestMapping.Method.GET, value = "/signout.do")
public class SignoutController implements BaseController {
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //회원탈퇴 페이지 구현: 로그인 재사용, 로그인과 반대로 세션이 있으면 페이지 유지/없으면 메인으로 redirect
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object id = session.getAttribute("loggedInAsUserId");
            log.debug("hello signout validation:{}/{},{}", session, id, req.getAttribute("wrongUserInput"));

            if (id != null) {
                //로그인되어있으면 회원탈퇴 폼으로
                return "shop/login/signout_form";

                //로그인되어있지않은 경우 메인으로 리다이렉트
            } else return "redirect:/index.do";
        } else return "redirect:/index.do";
    }
}
