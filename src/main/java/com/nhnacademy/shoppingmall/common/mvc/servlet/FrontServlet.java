package com.nhnacademy.shoppingmall.common.mvc.servlet;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.mvc.view.ViewResolver;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.mvc.controller.ControllerFactory;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

import static javax.servlet.RequestDispatcher.*;

@Slf4j
@WebServlet(name = "frontServlet",urlPatterns = {"*.do"})
public class FrontServlet extends HttpServlet {
    private ControllerFactory controllerFactory;
    private ViewResolver viewResolver;

    @Override
    public void init() throws ServletException {
        //todo#7-1 controllerFactory를 초기화 합니다.
        controllerFactory = (ControllerFactory) this.getServletContext()
                .getAttribute(ControllerFactory.CONTEXT_CONTROLLER_FACTORY_NAME);

        //todo#7-2 viewResolver를 초기화 합니다.
        viewResolver = new ViewResolver();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try{
            //todo#7-3 Connection pool로 부터 connection 할당 받습니다. connection은 Thread 내에서 공유됩니다.
            DbConnectionThreadLocal.initialize();
            log.debug("hello front:{}/connection {}", req.getRequestURI(), DbConnectionThreadLocal.getConnection());
            BaseController baseController = (BaseController) controllerFactory.getController(req);
            String viewName = baseController.execute(req,resp);

            if(viewResolver.isRedirect(viewName)){
                String redirectUrl = viewResolver.getRedirectUrl(viewName);
                log.debug("redirect,Url:{}", redirectUrl);
                //todo#7-6 redirect: 로 시작하면  해당 url로 redirect 합니다.
                resp.sendRedirect(redirectUrl);
            }else {
                String layout = viewResolver.getLayOut(viewName);
                log.debug("NOT redirect, VIEW :{}", layout);
                log.debug("NOT redirect, LAYOUT_CONTENT_HOLDER:{}", viewResolver.getPath(viewName));
                req.setAttribute(ViewResolver.LAYOUT_CONTENT_HOLDER, viewResolver.getPath(viewName));
                RequestDispatcher rd = req.getRequestDispatcher(layout);
                rd.include(req, resp);
            }
        } catch (Exception e) {
            //todo#7-5 예외가 발생하면 해당 예외에 대해서 적절한 처리를 합니다.
            //#todo error page에 실제 에러코드를 출력하도록 수정하면 좋을 것 같다.
            if (req.getAttribute(ERROR_STATUS_CODE) == null) {
                req.setAttribute("status_code", "500");
            } else
                req.setAttribute("status_code", req.getAttribute(ERROR_STATUS_CODE));

            req.setAttribute("exception_type", req.getAttribute(ERROR_EXCEPTION_TYPE));
            req.setAttribute("message", req.getAttribute(ERROR_MESSAGE));
            req.setAttribute("exception", req.getAttribute(ERROR_EXCEPTION));
            req.setAttribute("request_uri", req.getAttribute(ERROR_REQUEST_URI));
            log.error("status_code:{}", req.getAttribute(ERROR_STATUS_CODE));

            DbConnectionThreadLocal.setSqlError(true);

            String layout = viewResolver.getLayOut("/error");
            log.debug("ERROR, layout :{}", layout);
            RequestDispatcher rd = req.getRequestDispatcher(layout);
            rd.include(req, resp);

        } finally {
            //todo#7-4 connection을 반납합니다.
            try {
                log.debug("end front:{}/connection {}", req.getRequestURI(), DbConnectionThreadLocal.getConnection());
                DbConnectionThreadLocal.reset();
            } catch (SQLException e) {
                //throw new RuntimeException(e);
            }
        }
    }

}