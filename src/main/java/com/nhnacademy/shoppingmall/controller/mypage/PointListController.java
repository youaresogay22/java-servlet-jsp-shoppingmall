package com.nhnacademy.shoppingmall.controller.mypage;

import com.nhnacademy.shoppingmall.common.mvc.annotation.RequestMapping;
import com.nhnacademy.shoppingmall.common.mvc.controller.BaseController;
import com.nhnacademy.shoppingmall.common.page.PageContainer;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;
import com.nhnacademy.shoppingmall.user.pointdetail.repository.impl.PointDetailRepositoryImpl;
import com.nhnacademy.shoppingmall.user.pointdetail.service.PointDetailService;
import com.nhnacademy.shoppingmall.user.pointdetail.service.impl.PointDetailServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequestMapping(method = RequestMapping.Method.GET, value = {"/managePointDetail.do"})
@Slf4j
public class PointListController implements BaseController {
    PointDetailService pointservice = new PointDetailServiceImpl(new PointDetailRepositoryImpl());

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String page_temp = req.getParameter("page_temp");
        HttpSession session = req.getSession(false);

        if (session != null) {
            String userId = session.getAttribute("loggedInAsUserId").toString();
            setListAttribute(page_temp, userId, req);
            req.setAttribute("userId", userId);
        }

        return "/mypage/user/pointlist";
    }

    private void setListAttribute(String page_temp, String userId, HttpServletRequest req) {
        PageContainer container = pagination(page_temp, userId);

        req.setAttribute("list", container.getList().getContent());
        req.setAttribute("startpageno", container.getBlockStartPageNo());
        req.setAttribute("endpageno", container.getBlockEndPageNo());
        req.setAttribute("totalpages", container.getTotalPageCount());
        req.setAttribute("currentpage", container.getCurrentPage());
    }

    private PageContainer pagination(String page, String userId) {
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
        totalrowcount = pointservice.countUser(userId);
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
        return new PageContainer(pointservice.getAllPageOfUser(currentpage, pagesize, userId), totalpagecount, blockstartpageno, blockendpageno, currentpage);
    }
}
