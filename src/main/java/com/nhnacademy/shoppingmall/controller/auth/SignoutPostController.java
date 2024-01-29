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
@RequestMapping(method = RequestMapping.Method.POST, value = "/signoutAction.do")
public class SignoutPostController implements BaseController {

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //회원탈퇴 구현, 마지막으로 아이디와 비밀번호를 받아 체크합니다
        //틀린 경우 확인 메시지를 띄웁니다
        HttpSession session = req.getSession(false); //세션 확인
        if (session != null) {//세션 있으면 입력값과 로그인된 값 일치 확인, 혹은 다른 유저의 계정 삭제 방지
            String id = req.getParameter("user_id");
            String pw = req.getParameter("user_password");
            String sessionId = session.getAttribute("loggedInAsUserId").toString();
            String sessionPw = session.getAttribute("userPassword").toString();

            //입력값 = 로그인값 인 경우(제대로 입력한 경우)
            if (id.equals(sessionId) && pw.equals(sessionPw)) {
                try {// 서버에 해당 유저가 존재하는지 마지막으로 한번더 확인
                    User user = userService.doLogin(id, pw);
                    log.debug("bye signout:{},{}", id, pw);

                    if (user != null) {
                        try {//전부 통과했으면 유저 삭제
                            userService.deleteUser(id);
                            // 삭제 성공했으면 세션 정보 삭제까지 진행한 후 메인화면으로 redirect
                            session.removeAttribute("loggedInAsUserId");
                            session.removeAttribute("userPassword");
                            session.invalidate();
                            return "redirect:/index.do";

                        } catch (UserNotFoundException e) {
                            //(그럴일은 없겠지만)서버에서 유저 삭제 실패 시 경고문을 띄우려는 시도
                            log.debug("user find execption");
                            req.setAttribute("wrongUserInput", true);
                            return "shop/login/signout_form";
                        }

                    } else {
                        //unrechable code, catch에서 이미 처리됨
                        req.setAttribute("wrongUserInput", true);
                        return "shop/login/signout_form";
                    }

                } catch (UserNotFoundException e) {
                    log.debug("user find execption");
                    req.setAttribute("wrongUserInput", true);
                    return "shop/login/signout_form";
                }
            } else {// 입력값 일치하지 않는 경우 jstl 이용해 경고문구를 띄우기 위해 attribute 추가
                req.setAttribute("wrongUserInput", true);
                log.debug("hello test context:{}", req.getAttribute("wrongUserInput"));
                return "shop/login/signout_form";
            }
        } else //세션 없는 경우 메인화면으로 리다이렉트
            return "redirect:/index.do";
    }

}