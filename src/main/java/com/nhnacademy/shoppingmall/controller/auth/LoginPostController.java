package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.thread.channel.RequestChannel;
import com.nhnacademy.shoppingmall.thread.request.impl.PointChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

                //02.01: 포인트 적립 구현
                try {
                    if (isPointAddable(user.getLatestLoginAt())) {
                        PointChannelRequest request = new PointChannelRequest(user, "매일 첫 로그인 10,000P 적립", 10_000);
                        RequestChannel requestchannel = (RequestChannel) req.getServletContext().getAttribute("requestChannel");
                        requestchannel.addRequest(request);
                    }
                } catch (Exception e) {
                    log.debug("point add execption");
                    throw new RuntimeException(e);
                }

                return "/shop/main/index";
            }
        } catch (UserNotFoundException e) {
            log.debug("login execption");
            throw new UserNotFoundException("id");
        }

        return "redirect:/login.do";
    }

    private boolean isPointAddable(LocalDateTime latestloginat) {
        //테스트 코드, 분단위 포인트 적립
        LocalDateTime latestLoginAt = latestloginat.truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime now = LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES);

//        LocalDateTime latestLoginAt = latestloginat.truncatedTo(ChronoUnit.DAYS);
//        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        return latestloginat.isBefore(now);
    }
}

