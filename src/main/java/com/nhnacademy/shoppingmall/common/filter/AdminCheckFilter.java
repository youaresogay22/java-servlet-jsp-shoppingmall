package com.nhnacademy.shoppingmall.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.regex.Pattern;

@Slf4j
@WebFilter(
        filterName = "adminCheckFilter",
        urlPatterns = "/admin/*"
)

public class AdminCheckFilter extends HttpFilter {
    private static final String ADMIN_PATH = "/admin";
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //todo#11 /admin/ 하위 요청은 관리자 권한의 사용자만 접근할 수 있습니다. ROLE_USER가 접근하면 403 Forbidden 에러처리
        log.debug("hello a.check filter: {}", req.getRequestURI());

        if (isAdminPath(req.getRequestURI())) {
            log.debug("a.check filter:");
            HttpSession session = req.getSession(false);
            String role = req.getAttribute("user_auth").toString();
            if (session == null || role.equals("ROLE_USER")) {
                res.sendError(403, "forbidden");
            }
        } else chain.doFilter(req, res);
    }

    public boolean isAdminPath(String uri) {
        return StringUtils.containsIgnoreCase(uri, ADMIN_PATH);
    }
}
