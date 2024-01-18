package com.nhnacademy.shoppingmall;

import com.nhnacademy.shoppingmall.common.util.DbUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Slf4j
public class randomUserGenerator {
    String random_user_id, random_user_name, random_user_password, random_user_birth;
    Timestamp random_created_at;

    enum user_auth {
        USER("ROLE_USER"), ADMIN("ROLE_ADMIN");
        private final String role;

        user_auth(String role) {
            this.role = role;
        }

        public String getRole() {
            return this.role;
        }
    }

    public randomUserGenerator() {
        random_user_id = RandomStringUtils.randomAlphanumeric(10);
        random_user_name = RandomStringUtils.randomAlphabetic(7) + " " + RandomStringUtils.randomAlphabetic(6);
        random_user_password = RandomStringUtils.randomAlphanumeric(15);
        random_user_birth = stringRandomDate(generateRandomDate());
        user_auth random_user_auth = randomUserGenerator.user_auth.USER;
        random_created_at = generateRandomDate();
    }

    public Timestamp generateRandomDate() {
        Random random = new Random();
        Instant lowerBoundDate = Instant.parse("1900-01-01T00:00:00.00Z");
        Instant upperBoundDate = Instant.parse("2024-02-01T23:59:59.99Z");
        int days = (int) ChronoUnit.DAYS.between(lowerBoundDate, upperBoundDate);

        Instant randomInstant = lowerBoundDate.plus(random.nextInt(days - 1), ChronoUnit.DAYS);

        while (randomInstant.equals(lowerBoundDate)) {
            randomInstant = lowerBoundDate.plus(random.nextInt(days - 1), ChronoUnit.DAYS);
        }

        return Timestamp.from(randomInstant);
    }

    public String stringRandomDate(Timestamp date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        return df.format(date);
    }


    //      유저 데이터 생성기:
//      10글자의 userid,
//      성 7글자/이름 6글자의 이름,
//      15자리 암호,
//      1900~2024년 사이의 생년월일,
//      1900~2024/1/30 사이의 가입일자 생성
    public static void main(String[] args) throws SQLException {
        Connection testconn = DbUtils.getDataSource().getConnection();
        String sql = "INSERT INTO users (user_id, user_name, user_password, user_birth, user_auth, created_at)" +
                "values (?,?,?,?,?,?)";
        PreparedStatement preparedSql = testconn.prepareStatement(sql);

        for (int i = 0; i < 30; i++) {
            randomUserGenerator newUser = new randomUserGenerator();
            preparedSql.setString(1, newUser.random_user_id);
            preparedSql.setString(2, newUser.random_user_name);
            preparedSql.setString(3, newUser.random_user_password);
            preparedSql.setString(4, newUser.random_user_birth);
            preparedSql.setString(5, user_auth.USER.getRole());
            preparedSql.setTimestamp(6, newUser.random_created_at);
            log.debug("sql:{}", sql);

            preparedSql.executeUpdate();
        }


    }
}
