package com.nhnacademy.shoppingmall.thread.request.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.thread.request.ChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;
import com.nhnacademy.shoppingmall.user.pointdetail.repository.impl.PointDetailRepositoryImpl;
import com.nhnacademy.shoppingmall.user.pointdetail.service.PointDetailService;
import com.nhnacademy.shoppingmall.user.pointdetail.service.impl.PointDetailServiceImpl;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Slf4j
public class PointChannelRequest extends ChannelRequest {
    UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    PointDetailService pointService = new PointDetailServiceImpl(new PointDetailRepositoryImpl());

    @Getter
    private final User user;
    @Getter
    private final PointDetail.changeInfo changeType;
    private final int value;

    //no usage: 추후 적립 시 servlet context에서 requestChannel검색하여 본 클래스 생성한 뒤 addrequest로 삽입
    //02.01 수정: 로그읹 적립 구현, 일부 필드 추가
    //02.02 수정: pointdetail 사용하도록 수정
    public PointChannelRequest(User user, PointDetail.changeInfo changeType, int value) {
        this.user = user;
        this.changeType = changeType;
        this.value = value;
    }

    @Override
    public void execute() throws SQLException {
        DbConnectionThreadLocal.initialize();
        //todo#14-5 포인트 적립구현, connection은 point적립이 완료되면 반납합니다.
        log.debug("pointChannel execute");
        RequestContainer container = new RequestContainer(user, changeType, value);

        try {
            ((UserServiceImpl) userService).updateUserPoint(container.getUser());
            pointService.save(container.getPointDetail());
        } catch (Exception e) {
            //error 발생 시 커밋 롤백
            DbConnectionThreadLocal.setSqlError(true);
        }

        DbConnectionThreadLocal.reset();
    }

    private String generateRequestId(LocalDateTime time) {
        String requestId = StringUtils.getDigits(super.toString());
        return user.getUserId() + ":" + time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss:")) + requestId;
    }

    @Getter
    private class RequestContainer {
        private final User user;
        private final PointDetail pointDetail;

        private RequestContainer(User user, PointDetail.changeInfo changeType, int value) {
            LocalDateTime nowTime = LocalDateTime.now();

            this.pointDetail = new PointDetail(
                    generateRequestId(nowTime),
                    user.getUserId(),
                    changeType,
                    BigDecimal.valueOf(value),
                    nowTime
            );
            user.setUserPoint(user.getUserPoint() + pointDetail.getChangeAmount().intValue());
            this.user = user;
        }

    }

}
