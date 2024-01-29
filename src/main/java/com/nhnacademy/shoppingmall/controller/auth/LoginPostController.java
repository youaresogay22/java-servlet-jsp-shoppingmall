package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(method = RequestMapping.Method.POST,value = "/loginAction.do")
public class LoginPostController implements BaseController {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //todo#13-2 로그인 구현, session은 60분동안 유지됩니다.
        String id = req.getParameter("user_id");
        String pw = req.getParameter("user_password");

        try {
            User user = userService.doLogin(id, pw);

            if (user != null) {
                log.debug("hello login:{}/{}", id, pw);
                
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedInAsUserId", id);
                session.setAttribute("userPassword", pw);
                session.setAttribute("userAuth", user.getUserAuth().toString());
                session.setMaxInactiveInterval(60 * 60);
                return "redirect:/index.do";
            }
        } catch (UserNotFoundException e) {
            log.debug("login execption");
        }

        return "redirect:/login.do";
    }
}

