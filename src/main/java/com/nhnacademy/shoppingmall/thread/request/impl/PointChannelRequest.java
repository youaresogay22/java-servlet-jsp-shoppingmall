package com.nhnacademy.shoppingmall.thread.request.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.thread.request.ChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.service.impl.UserServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class PointChannelRequest extends ChannelRequest {
    UserService userService = new UserServiceImpl(new UserRepositoryImpl());
    @Getter
    private final User user;
    @Getter
    private final String requestId;
    @Getter
    private final String changeInfo;


    //no usage: 추후 적립 시 servlet context에서 requestChannel검색하여 본 클래스 생성한 뒤 addrequest로 삽입
    //02.01 수정: 로그읹 적립 구현, 일부 필드 추가
    public PointChannelRequest(User user, String changeInfo, int changeAmount) {
        user.setUserPoint(user.getUserPoint() + changeAmount);
        this.user = user;
        this.requestId = generateRequestId(LocalDateTime.now());
        this.changeInfo = changeInfo;
    }

    @Override
    public void execute() throws SQLException {
        DbConnectionThreadLocal.initialize();
        //todo#14-5 포인트 적립구현, connection은 point적립이 완료되면 반납합니다.
        log.debug("pointChannel execute");

        ((UserServiceImpl) userService).updateUserPoint(user, this);
        DbConnectionThreadLocal.reset();
    }

    public String generateRequestId(LocalDateTime time) {
        String requestId = StringUtils.getDigits(super.toString());
        return user.getUserId() + ":" + time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss:")) + requestId;
    }
}
