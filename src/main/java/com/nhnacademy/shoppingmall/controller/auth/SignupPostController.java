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
import java.time.LocalDateTime;

@Slf4j
@RequestMapping(method = RequestMapping.Method.POST, value = "/signupAction.do")
public class SignupPostController implements BaseController {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //회원가입 구현, 아이디와 비밀번호를 받고 아이디를 데이터베이스에서 중복 검사합니다.
        //틀린 경우 확인 요구 메시지를 띄웁니다
        String id = req.getParameter("user_id");
        String pw = req.getParameter("user_password");
        String name = req.getParameter("user_name");
        String birth = req.getParameter("user_birth");

        // DB에 해당 id가 존재하는지 중복 검사
        User user = userService.getUser(id);
        if (user == null) {
            try {// 중복 없으면 등록 진행
                User newuser = new User(id, name, pw, birth, User.Auth.ROLE_USER, 1_000_000, LocalDateTime.now(), null);
                userService.saveUser(newuser);
                log.debug("hello signup :{},{}", newuser.getUserId(), newuser.getUserPassword());
                // 등록 성공했으면 로그인 진행 후 index로 리다이렉트해줌
                //임시코드, loginpostcontroller로 넘겨주는 편이 깔끔할 것 같음
                HttpSession session = req.getSession(true);
                session.setAttribute("loggedInAsUserId", newuser.getUserId());
                session.setAttribute("userPassword", newuser.getUserPassword());
                session.setAttribute("userAuth", newuser.getUserAuth().toString());
                session.setMaxInactiveInterval(60 * 60);
                return "redirect:/index.do";

            } catch (UserNotFoundException e) {
                //유저 등록 실패 시 오류 처리 부분
                log.debug("user save execption");
                return "shop/login/signout_form";
            }

        } else {
            //입력한 id의 중복이 존재하면 알림 메시지 띄우기 위해 설정값 추가
            req.setAttribute("duplicateId", true);
            return "shop/login/signup_form";
        }
    }
}

