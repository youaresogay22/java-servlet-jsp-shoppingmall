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
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@WebServlet(name = "frontServlet",urlPatterns = {"*.do"})
public class FrontServlet extends HttpServlet {
    private ControllerFactory controllerFactory;
    private ViewResolver viewResolver;

    @Override
    public void init() throws ServletException {
        //todo#7-1 controllerFactory를 초기화 합니다.
        controllerFactory = (ControllerFactory) getServletContext().getAttribute("CONTEXT_CONTROLLER_FACTORY");

        //todo#7-2 viewResolver를 초기화 합니다.
        viewResolver = new ViewResolver();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        try{
            //todo#7-3 Connection pool로 부터 connection 할당 받습니다. connection은 Thread 내에서 공유됩니다.
            DbConnectionThreadLocal.initialize();
            Connection connection = DbConnectionThreadLocal.getConnection();

            BaseController baseController = (BaseController) controllerFactory.getController(req);
            String viewName = baseController.execute(req,resp);

            if(viewResolver.isRedirect(viewName)){
                String redirectUrl = viewResolver.getRedirectUrl(viewName);
                log.debug("redirectUrl:{}",redirectUrl);
                //todo#7-6 redirect: 로 시작하면  해당 url로 redirect 합니다.
                resp.sendRedirect(redirectUrl);
            }else {
                String layout = viewResolver.getLayOut(viewName);
                log.debug("viewName:{}", viewResolver.getPath(viewName));
                req.setAttribute(ViewResolver.LAYOUT_CONTENT_HOLDER, viewResolver.getPath(viewName));
                RequestDispatcher rd = req.getRequestDispatcher(layout);
                rd.include(req, resp);
            }
        }catch (Exception e){
            DbConnectionThreadLocal.setSqlError(true);
            //todo#7-5 예외가 발생하면 해당 예외에 대해서 적절한 처리를 합니다.
            Object exception = req.getAttribute("javax.servlet.error.exception");
            Object exceptionType = req.getAttribute("javax.servlet.error.exception_type");
            Object message = req.getAttribute("javax.servlet.error.message");
            Object requestUri = req.getAttribute("javax.servlet.error.request_uri");
            Object servletName = req.getAttribute("javax.servlet.error.servlet_name");
            Object statusCode = req.getAttribute("javax.servlet.error.status_code");
            log.error("error:{}", exception);
            log.error("exceptionType:{}", exceptionType);
            log.error("message:{}", message);
            log.error("requestUri:{}", requestUri);
            log.error("servletName:{}", servletName);

            try {
                resp.sendError((Integer) statusCode, statusCode + "오류. 관리자에게 문의하지 마십시오");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }finally {
            //todo#7-4 connection을 반납합니다.
            try {
                DbConnectionThreadLocal.reset();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}