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
                User user = userService.getUser(id);

                if (user != null) {// requiered field: password
                    String pw = req.getParameter("userPassword");
                    String name = req.getParameter("user_name");
                    log.debug("hello manage profile:{}/{}/{}", id, pw, name);

                    if (pw == null)
                        return "/mypage/user/manage_profile";

                    if (name == null) {// 이름은 입력하지 않아도 수정가능하므로 체크
                        User managed_user = new User(id, user.getUserName(), pw, user.getUserBirth(),
                                user.getUserAuth(), user.getUserPoint(), user.getCreatedAt(), user.getLatestLoginAt());
                        userService.updateUser(managed_user);
                    } else {
                        User managed_user = new User(id, name, pw, user.getUserBirth(),
                                user.getUserAuth(), user.getUserPoint(), user.getCreatedAt(), user.getLatestLoginAt());
                        userService.updateUser(managed_user);
                    }
                    return "redirect:/mypage.do";
                }
                return "/mypage/user/manage_profile"; //user==null

            } catch (UserNotFoundException e) {
                log.debug("user find execption");
                throw new UserNotFoundException("id");
            }
        } else return "/mypage/user/manage_profile";//session==null
    }
}
