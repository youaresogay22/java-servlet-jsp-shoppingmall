package com.nhnacademy.shoppingmall.controller.mypage;

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
@RequestMapping(method = RequestMapping.Method.POST, value = {"/manageProfileAction.do"})
public class manageProfilePostController implements BaseController {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        //02.01: 회원정보 수정 구현
        HttpSession session = req.getSession(false);

        if (session != null) {
            try {
                String id = session.getAttribute("loggedInAsUserId").toString();
                String pw = req.getParameter("user_password");
                User user = userService.getUser(id);

                if (pw == null) {// pw==null
                    req.setAttribute("noPassword", true);
                    return "/mypage/user/manage_profile";
                }

                if (user == null) {// login error
                    req.setAttribute("wrongPassword", true);
                    return "/mypage/user/manage_profile";
                }

                String name = req.getParameter("user_name");
                log.debug("hello manage profile:{}/{}/{}", id, pw, name);

                User managed_user;
                if (name == null) {// 이름은 입력하지 않아도 수정가능하므로 체크
                    managed_user = new User(id, user.getUserName(), pw, user.getUserBirth(),
                            user.getUserAuth(), user.getUserPoint(), user.getCreatedAt(), user.getLatestLoginAt());
                } else {
                    managed_user = new User(id, name, pw, user.getUserBirth(),
                            user.getUserAuth(), user.getUserPoint(), user.getCreatedAt(), user.getLatestLoginAt());
                }
                userService.updateUser(managed_user);
                //수정 성공한경우 로그인된 세션값 역시 변경
                session.setAttribute("loggedInAsUserId", managed_user.getUserId());
                session.setAttribute("userPassword", managed_user.getUserPassword());
                session.setAttribute("userAuth", managed_user.getUserAuth().toString());
                //최종변경 이후 마이페이지로
                return "redirect:/mypage.do";

            } catch (UserNotFoundException e) {//database error
                log.debug("user find execption");
                throw new UserNotFoundException("id");
            }
        } else return "/mypage/user/manage_profile";//session==null
    }
}
