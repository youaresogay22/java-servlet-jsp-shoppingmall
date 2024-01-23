package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@RequestMapping(method = RequestMapping.Method.POST,value = "/loginAction.do")
public class LoginPostController implements BaseController {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //todo#13-2 로그인 구현, session은 60분동안 유지됩니다.
        try {
            DbConnectionThreadLocal.initialize();
            HttpSession session = req.getSession(true);
            String id = req.getParameter("userId");
            String pw = req.getParameter("userPassword");

            User user = userService.doLogin(id, pw);

            if (user != null) {
                session.setAttribute("loggedInAsUserId", id);
                session.setAttribute("userPassword", pw);
                session.setMaxInactiveInterval(60 * 60);
                return "shop/main/index";
            } else return "shop/login/login_form";

        } catch (SQLException e) {
            DbConnectionThreadLocal.setSqlError(true);
            throw new RuntimeException(e);
        } finally {
            try {
                DbConnectionThreadLocal.reset();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
