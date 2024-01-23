package com.nhnacademy.shoppingmall.common.mvc.controller;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.exception.ControllerNotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class ControllerFactory {
    public static final String CONTEXT_CONTROLLER_FACTORY_NAME="CONTEXT_CONTROLLER_FACTORY";
    private final ConcurrentMap<String, Object> beanMap = new ConcurrentHashMap<>();

    public void initialize(Set<Class<?>> c, ServletContext ctx) {

        if(Objects.isNull(c)){
            log.info("Controller not found");
            return;
        }

        /*todo#5-1 ControllerFactory 초기화, 아래 설명을 참고하여 구현합니다.
         * 1. Set<Class<?>> c 에는 com.nhnacademy.shoppingmall.common.initialize.WebAppInitializer 에서  HandlesTypes에
         * com.nhnacademy.shoppingmall.common.mvc.controller.BaseController.class인 class를 set에 담겨서 parameter로 전달 됩니다.
         * BaseController를 구현한 Controller class가 전달됩니다.
         * 2.Java Reflection API를 사용하여 Controller class의 instance를 생성하고 beanMap에 등록합니다. key/value는 다음과 같습니다.
         * ex) key= GET-/index.do , value = IndexController's instance
         * 3. @RequestMapping(method = RequestMapping.Method.GET,value = {"/index.do","/main.do"}) 처럼 value는 String 배열일 수 있습니다.
         * 즉 /index.do, /main.do -> IndexController로 맵핑 됩니다.
         */

        //참고문헌: https://www.tutorialspoint.com/javareflect/javareflect_method_getannotation.htm
        try {
            for (Class<?> some_classes_in_set : c) {
                BaseController baseController = (BaseController) some_classes_in_set.getDeclaredConstructor().newInstance();
                RequestMapping annotation = some_classes_in_set.getAnnotation(RequestMapping.class);
                log.debug("기열 어노테이션:{}", annotation);

                for (String value : annotation.value()) {
                    beanMap.put(getKey(annotation.method().toString(), value), baseController);
                }
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        //todo#5-2 ctx(ServletContext)에  attribute를 추가합니다. -> key : CONTEXT_CONTROLLER_FACTORY_NAME, value : ControllerFactory
        ctx.setAttribute(CONTEXT_CONTROLLER_FACTORY_NAME, this);
    }

    private Object getBean(String key){
        //todo#5-3 beanMap에서 controller 객체를 반환 합니다.
        if (beanMap.containsKey(key)) {
            return beanMap.get(key);
        } else {
            throw new ControllerNotFoundException(key);
        }
    }

    public Object getController(HttpServletRequest request){
        //todo#5-4 request의 method, servletPath를 이용해서 Controller 객체를 반환합니다.
        String requestkey = request.getMethod() + "-" + request.getServletPath();
        if (beanMap.containsKey(requestkey)) {
            return beanMap.get(requestkey);
        } else {
            throw new ControllerNotFoundException(requestkey);
        }
    }

    public Object getController(String method, String path){
        //todo#5-5 method, path를 이용해서 Controller 객체를 반환 합니다.
        String requestkey = getKey(method, path);
        if (beanMap.containsKey(requestkey)) {
            return beanMap.get(requestkey);
        } else {
            throw new ControllerNotFoundException(requestkey);
        }
    }

    private String getKey(String method, String path){
        //todo#5-6  {method}-{key}  형식으로 Key를 반환 합니다.
        //ex GET-/index.do
        //ex POST-/loginAction.do
        return method + "-" + path;
    }
}
