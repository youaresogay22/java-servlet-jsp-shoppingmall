package com.nhnacademy.shoppingmall.user;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Slf4j
public class randomUserGenerator {

    public randomUserGenerator() {

    }

    public static LocalDateTime generateRandomDate() {
        Random random = new Random();
        Instant lowerBoundDate = Instant.parse("1900-01-01T00:00:00.00Z");
        Instant upperBoundDate = Instant.parse("2024-02-01T23:59:59.99Z");
        int days = (int) ChronoUnit.DAYS.between(lowerBoundDate, upperBoundDate);

        Instant randomInstant = lowerBoundDate.plus(random.nextInt(days - 1), ChronoUnit.DAYS);

        while (randomInstant.equals(lowerBoundDate)) {
            randomInstant = lowerBoundDate.plus(random.nextInt(days - 1), ChronoUnit.DAYS);
        }

        return LocalDateTime.ofInstant(randomInstant, ZoneOffset.UTC);
    }

    public static String stringRandomDate(LocalDateTime date) {
        return date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }


    //          유저 데이터 생성기:
//      10글자의 userid,
//      성 7글자/이름 6글자의 이름,
//      15자리 암호,
//      1900~2024년 사이의 생년월일,
//      1900~2024/1/30 사이의 가입일자 생성
//    update 1-19: User/DbConnectionThreadLocal 사용하도록 수정

    public static void main(String[] args) throws SQLException {
        DbConnectionThreadLocal.initialize();

        String sql = "INSERT INTO users (user_id, user_name, user_password, " +
                "user_birth, user_auth, user_point, " +
                "created_at) " +
                "VALUES (?,?,?,?,?,?,?)";
        Connection testconn = DbConnectionThreadLocal.getConnection();
        PreparedStatement preparedSql = testconn.prepareStatement(sql);

        for (int i = 0; i < 20; i++) {
            User randomUser = new User(
                    RandomStringUtils.randomAlphanumeric(10),
                    RandomStringUtils.randomAlphabetic(7) + " " + RandomStringUtils.randomAlphabetic(6),
                    RandomStringUtils.randomAlphanumeric(15),
                    stringRandomDate(generateRandomDate()),
                    User.Auth.ROLE_USER,
                    1_000_000,
                    generateRandomDate(),
                    null
            );
            preparedSql.setString(1, randomUser.getUserId());
            preparedSql.setString(2, randomUser.getUserName());
            preparedSql.setString(3, randomUser.getUserPassword());
            preparedSql.setString(4, randomUser.getUserBirth());
            preparedSql.setString(5, randomUser.getUserAuth().toString());
            preparedSql.setInt(6, randomUser.getUserPoint());
            preparedSql.setTimestamp(7, Timestamp.valueOf(randomUser.getCreatedAt()));
            log.debug("connection:{}", testconn);
            log.debug("sql:{}", preparedSql);
            preparedSql.executeUpdate();
        }
        DbConnectionThreadLocal.reset();
    }
}
