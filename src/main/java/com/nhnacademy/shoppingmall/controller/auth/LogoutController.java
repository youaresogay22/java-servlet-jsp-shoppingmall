package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Slf4j
@RequestMapping(method = RequestMapping.Method.POST, value = "/logout.do")
public class LogoutController implements BaseController {
    //todo#13-3 로그아웃 구현
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        log.debug("hello logout");
        HttpSession session = req.getSession(false);
        if (session != null) {
            Object id = session.getAttribute("loggedInAsUserId");
            log.debug("bye logout:{}/{}", session, id);

            if (id != null) {
                session.removeAttribute("loggedInAsUserId");
                session.removeAttribute("userPassword");
                session.invalidate();
                return "redirect:/index.do";
            } else return "shop/login/login_form";

        } else return "shop/login/login_form";
    }
}
