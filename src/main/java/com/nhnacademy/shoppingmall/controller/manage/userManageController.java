package com.nhnacademy.shoppingmall.controller.manage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
@RequestMapping(method = RequestMapping.Method.GET, value = "/userManage.do")
public class userManageController implements BaseController {

    private final UserServiceImpl userService = new UserServiceImpl(new UserRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        // 페이지 탐색을 위해 파라미터로 받을 수 있도록 함
        //HttpSession session = req.getSession(true);
        String page_admin = req.getParameter("page_admin");
        String page_user = req.getParameter("page_user");

        setListAttribute(page_admin, page_user, req);

        return "/shop/main/admin/userlist";
    }

    private void setListAttribute(String page_admin, String page_user, HttpServletRequest req) {
        pageContainer admincontainer = pagination(page_admin, User.Auth.ROLE_ADMIN.toString());
        pageContainer usercontainer = pagination(page_user, User.Auth.ROLE_USER.toString());

        req.setAttribute("list_user", usercontainer.getList().getContent());
        req.setAttribute("startpageno_user", usercontainer.getBlockStartPageNo());
        req.setAttribute("endpageno_user", usercontainer.getBlockEndPageNo());
        req.setAttribute("totalpages_user", usercontainer.getTotalPageCount());
        req.setAttribute("currentpage_user", usercontainer.getCurrentPage());

        req.setAttribute("list_admin", admincontainer.getList().getContent());
        req.setAttribute("startpageno_admin", admincontainer.getBlockStartPageNo());
        req.setAttribute("endpageno_admin", admincontainer.getBlockEndPageNo());
        req.setAttribute("totalpages_admin", admincontainer.getTotalPageCount());
        req.setAttribute("currentpage_admin", admincontainer.getCurrentPage());
    }

    @Getter
    private static class pageContainer {
        private Page<User> list;
        private long totalPageCount;
        private long blockStartPageNo;
        private long blockEndPageNo;
        private long currentPage;

        pageContainer(Page<User> list, long totalpagecount, long blockstartpageno, long blockendpageno, long currentPage) {
            this.list = list;
            this.totalPageCount = totalpagecount;
            this.blockStartPageNo = blockstartpageno;
            this.blockEndPageNo = blockendpageno;
            this.currentPage = currentPage;
        }

    }

    private pageContainer pagination(String page, String role) {
        //parameter page= 현재 페이지, request parameter
        //현재 페이지 번호, 페이지당 row 수, 한번에 표시할 페이지 수: 직접 assign하여 사용
        int currentpage, pagesize, blocksize;
        //총 row 갯수, 이로부터 계산한 총 페이지 수, 페이지를 시작하는 첫 row 번호
        long totalrowcount, totalpagecount, pagestartrowno;
        // 페이지 블록마다 있는 시작 페이지,끝 페이지의 첫 row 번호
        long blockstartpageno, blockendpageno;
        //현재 페이지 블록 변수
        long currentblock;

        // page,block size assign
        pagesize = 10;
        blocksize = 5;

        try {// page parameter 비정상 값 처리 부분
            if (page == null || page.isEmpty()) {//null, 크기 0 처리
                currentpage = 1;
            }
            currentpage = Integer.parseInt(page);
        } catch (NumberFormatException e) {// 숫자가 아닌 값 처리
            currentpage = 1;
        }

        // 총 페이지 수 계산
        totalrowcount = userService.getAuthCount(role);
        // 총 row/page size 해서 나누어 떨어지면 몫을, 아니면 +1페이지에 나머지를 넣어야 함
        if (totalrowcount % pagesize == 0) {
            totalpagecount = totalrowcount / pagesize;
        } else
            totalpagecount = (totalrowcount / pagesize) + 1;

        // 총 페이지 수 비정상 값 처리 부분
        if (totalpagecount == 0) {
            totalpagecount = 1;
        }
        if (currentpage > totalpagecount) {
            currentpage = 1;
        }
        // 페이지 시작 지점 계산
        pagestartrowno = (long) (currentpage - 1) * pagesize;

        // 블록당 페이지 시작과 끝을 계산
        if (currentpage % blocksize == 0) {
            currentblock = currentpage / blocksize;
        } else
            currentblock = (currentpage / blocksize) + 1;
        // 블록 시작,끝 페이지 계산
        blockstartpageno = (currentblock - 1) * blocksize + 1;
        blockendpageno = blockstartpageno + blocksize - 1;

        // 마지막 블록에서 남은 row가 출력해야할 최대값보다 적으면 끝 페이지를 조정해 준다
        if (blockendpageno > totalpagecount) {
            blockendpageno = totalpagecount;
        }
        log.debug("page:{}, totalrow:{},pagestartrow:{}, blockstartpage:{}, blockendpageno:{}", page, totalrowcount, pagestartrowno, blockstartpageno, blockendpageno);
        return new pageContainer(userService.getUserPage(currentpage, pagesize, role), totalpagecount, blockstartpageno, blockendpageno, currentpage);
    }
}
