package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(method = RequestMapping.Method.GET, value = "/signup.do")
public class SignupController implements BaseController {
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        // 회원가입 구현, 세션이 없고 로그인상태도 아니면 회원가입 폼으로 이동,
        // 로그인 상태이면 메인으로 리다이렉트 합니다.
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object id = session.getAttribute("loggedInAsUserId");
            log.debug("hello signup validation:{}/{}", session, id);

            if (id != null) {
                return "redirect:/index.do";
            } else return "shop/login/signup_form";

        } else return "redirect:/index.do";
    }
}
