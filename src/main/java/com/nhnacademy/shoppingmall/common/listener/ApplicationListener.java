package com.nhnacademy.shoppingmall.common.listener;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
@WebListener
public class ApplicationListener implements ServletContextListener {
    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //todo#12 application 시작시 테스트 계정인 admin,user 등록합니다. 만약 존재하면 등록하지 않습니다.
        UserServiceImpl userService = new UserServiceImpl(new UserRepositoryImpl());
        try {
            DbConnectionThreadLocal.initialize();

            User testAdmin = new User("admin", "관리자", "12345",
                    "19000101", User.Auth.ROLE_ADMIN, 1_000_000,
                    LocalDateTime.now(), null);
            User testUser = new User("user", "사용자", "12345",
                    "19000102", User.Auth.ROLE_USER, 1_000_000,
                    LocalDateTime.now(), null);

            if (userService.getUser("admin") == null) {
                userService.saveUser(testAdmin);
            }
            if (userService.getUser("user") == null) {
                userService.saveUser(testUser);
            }
            
            DbConnectionThreadLocal.reset();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
