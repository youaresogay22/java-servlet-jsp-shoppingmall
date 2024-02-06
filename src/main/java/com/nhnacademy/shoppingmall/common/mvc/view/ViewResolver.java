package com.nhnacademy.shoppingmall.common.mvc.view;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class ViewResolver {

    public static final String DEFAULT_PREFIX="/WEB-INF/views/";
    public static final String DEFAULT_POSTFIX=".jsp";
    public static final String REDIRECT_PREFIX="redirect:";
    public static final String DEFAULT_SHOP_LAYOUT="/WEB-INF/views/layout/shop.jsp";
    public static final String DEFAULT_ADMIN_LAYOUT="/WEB-INF/views/layout/admin.jsp";
    public static final String DEFAULT_ERROR_LAYOUT = "/WEB-INF/views/layout/error.jsp";
    public static final String LAYOUT_CONTENT_HOLDER = "layout_content_holder";

    private final String prefix;
    private final String postfix;

    public ViewResolver(){
        this(DEFAULT_PREFIX,DEFAULT_POSTFIX);
    }
    public ViewResolver(String prefix, String postfix) {
        this.prefix = prefix;
        this.postfix = postfix;
    }

    public  String getPath(String viewName){
        //todo#6-1  prefix+viewName+postfix 반환 합니다.
        if (viewName.startsWith("/")) {
            return prefix + viewName.substring(1) + postfix;
        } else return prefix + viewName + postfix;
    }

    public boolean isRedirect(String viewName){
        //todo#6-2 REDIRECT_PREFIX가 포함되어 있는지 체크 합니다.
        return StringUtils.containsIgnoreCase(viewName, REDIRECT_PREFIX);
    }

    public String getRedirectUrl(String viewName){
        //todo#6-3 REDIRECT_PREFIX를 제외한 url을 반환 합니다.
        if (isRedirect(viewName)) {
            return viewName.substring(REDIRECT_PREFIX.length());
        } else return viewName;
    }

    public String getLayOut(String viewName){
        /*todo#6-4 viewName에
           /admin/경로가 포함되었다면 DEFAULT_ADMIN_LAYOUT 반환 합니다.
           /admin/경로가 포함되어 있지않다면 DEFAULT_SHOP_LAYOUT 반환 합니다.
        */
        //02.06 추가: error 포함되어있으면 error layout 반환
        if (StringUtils.containsIgnoreCase(viewName, "/admin")) {
            return DEFAULT_ADMIN_LAYOUT;
        } else if (StringUtils.containsIgnoreCase(viewName, "error")) {
            return DEFAULT_ERROR_LAYOUT;
        } else return DEFAULT_SHOP_LAYOUT;
    }
}
