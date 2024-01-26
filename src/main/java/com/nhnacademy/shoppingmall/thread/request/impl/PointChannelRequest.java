package com.nhnacademy.shoppingmall.thread.request.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.thread.request.ChannelRequest;
import com.nhnacademy.shoppingmall.user.domain.User;
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
    private final User user;
    private final String changeInfo;

    //    no usage: 추후 적립 시 servleT context에서 requestChannel검색하여 본 클래스 생성한 뒤 addrequest로 삽입
//    따라서 현재 기능만 구현되었고 no usage warning 뜨는것 해결 불가능
    public PointChannelRequest(User user, String changeInfo) {
        this.user = user;
        this.changeInfo = changeInfo;
    }

    @Override
    public void execute() throws SQLException {
        DbConnectionThreadLocal.initialize();
        //todo#14-5 포인트 적립구현, connection은 point적립이 완료되면 반납합니다.
        log.debug("pointChannel execute");

        String sql = "INSERT INTO userpointDetail (userpointDetailID, userID, " +
                "userpointchange, userpointchangedate)" +
                "VALUES (?,?,?,?)";

        Connection connection = DbConnectionThreadLocal.getConnection();
        PreparedStatement psmt = connection.prepareStatement(sql);

        LocalDateTime dateNow = LocalDateTime.now();
        psmt.setString(1, generateRequestId(dateNow));
        psmt.setString(2, user.getUserId());
        psmt.setString(3, changeInfo);
        psmt.setTimestamp(4, Timestamp.valueOf(dateNow));
        log.debug("point sql:{}", psmt);

        psmt.executeUpdate();
        DbConnectionThreadLocal.reset();
    }

    public String generateRequestId(LocalDateTime time) {
        String requestId = StringUtils.getDigits(super.toString());
        return user.getUserId() + ":" + time.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss:")) + requestId;
    }
}
