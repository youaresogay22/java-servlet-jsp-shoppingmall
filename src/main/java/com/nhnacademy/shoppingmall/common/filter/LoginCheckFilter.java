package com.nhnacademy.shoppingmall.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@WebFilter(
        filterName = "loginCheckFilter",
        urlPatterns = {"/mypage/*", "/mypage.do"}
)

public class LoginCheckFilter extends HttpFilter {
    private static final String MYPAGE_PATH = "mypage";
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //todo#10 /mypage/ 하위경로의 접근은 로그인한 사용자만 접근할 수 있습니다.
        log.debug("hello l.check filter: {}/{}", req.getRequestURI(), req.getSession(false));

        if (isMypagePath(req.getRequestURI())) {
            HttpSession session = req.getSession(false);

            if (session != null) {
                log.debug("hello l.check filter: {}", session.getAttribute("loggedInAsUserId"));
                if (session.getAttribute("loggedInAsUserId") != null) {
                    chain.doFilter(req, res);
                } else
                    res.sendRedirect("/index.do");
            } else
                res.sendRedirect("/index.do");
        }
    }

    public boolean isMypagePath(String uri) {
        return StringUtils.containsIgnoreCase(uri, MYPAGE_PATH);
    }
}