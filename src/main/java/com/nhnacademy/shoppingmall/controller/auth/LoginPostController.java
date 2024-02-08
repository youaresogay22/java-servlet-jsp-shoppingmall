package com.nhnacademy.shoppingmall.controller.auth;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.thread.channel.RequestChannel;
import com.nhnacademy.shoppingmall.thread.request.impl.PointChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;
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
                //02.02: pointdetail class 사용하도록 수정
                try {
                    if (isPointAddable(user.getLatestLoginAt())) {
                        PointChannelRequest request = new PointChannelRequest(user, PointDetail.changeInfo.DAILY_FIRST_LOGIN, 10_000);
                        RequestChannel requestchannel = (RequestChannel) req.getServletContext().getAttribute("requestChannel");
                        requestchannel.addRequest(request);
                    }
                } catch (Exception e) {
                    log.debug("point add execption");
                    throw new RuntimeException(e);
                }
                return "/shop/main/index";

                //02.08 수정: 로그인 에러 처리 수정(기존: 500에러 출력>현재 로그인 메시지 출력)
            } else throw new UserNotFoundException(id);
        } catch (UserNotFoundException e) {
            log.debug("login execption");
            req.setAttribute("userInputError", "true");
            return "shop/login/login_form";
        }
    }

    private boolean isPointAddable(LocalDateTime latestloginat) {
        //테스트 코드, 분당 1회 > 로그인당 1회씩 적립
//        LocalDateTime latestLoginAt = latestloginat.truncatedTo(ChronoUnit.MINUTES);
//        LocalDateTime now = LocalDateTime.now().plusMinutes(3).truncatedTo(ChronoUnit.MINUTES);

        LocalDateTime latestLoginAt = latestloginat.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        return latestLoginAt.isBefore(now);
    }
}

