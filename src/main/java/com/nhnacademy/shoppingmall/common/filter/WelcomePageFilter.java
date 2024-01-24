package com.nhnacademy.shoppingmall.common.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(
        filterName = "welcomePageFilter",
        urlPatterns = "/"
)

public class WelcomePageFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        //todo#9 /요청이 오면 welcome page인 index.do redirect 합니다.
        log.debug("hello welcome filter: {}", req.getRequestURI());

        if (req.getRequestURI().equals("/")) {
            log.debug("welcome filter: redirect...");
            res.sendRedirect("/index.do");
        } else chain.doFilter(req, res);
    }
}
