package com.nhnacademy.shoppingmall.user.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepositoryImpl implements UserRepository {

    @Override
    public Optional<User> findByUserIdAndUserPassword(String userId, String userPassword) {
//      todo#3-1 회원의 아이디와 비밀번호를 이용해서 조회하는 코드 입니다.(로그인)
//      해당 코드는 SQL Injection이 발생합니다. SQL Injection이 발생하지 않도록 수정하세요.

        String sql = "SELECT *" +
                "FROM users " +
                "WHERE user_id=? and user_password=?";
        try {
            return findUserQuery(sql, userId, userPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        //todo#3-2 회원조회
        String sql = "SELECT * " +
                "FROM users " +
                "WHERE user_id=?";

        try {
            return findUserQuery(sql, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int save(User user) {
        //todo#3-3 회원등록, executeUpdate()을 반환합니다.
        String sql = "INSERT INTO users (user_id, user_name, user_password, " +
                "user_birth, user_auth, user_point, " +
                "created_at) " +
                "VALUES (?,?,?,?,?,?,?)";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, user.getUserId());
            psmt.setString(2, user.getUserName());
            psmt.setString(3, user.getUserPassword());
            psmt.setString(4, user.getUserBirth());
            psmt.setString(5, user.getUserAuth().toString());
            psmt.setInt(6, user.getUserPoint());
            psmt.setTimestamp(7, Timestamp.valueOf(user.getCreatedAt()));
            log.debug("save sql:{}", psmt);
            log.debug("connection:{}", connection);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(String userId) {
        //todo#3-4 회원삭제, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "DELETE FROM users " +
                "WHERE user_id = ?";

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, userId);
            log.debug("DELETE sql:{}", psmt);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(User user) {
        //todo#3-5 회원수정, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "UPDATE users " +
                "SET user_name=?, user_password=?, users.user_birth=?, " +
                "users.user_auth=?, users.user_point=? " +
                "WHERE user_id=?";

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, user.getUserName());
            psmt.setString(2, user.getUserPassword());
            psmt.setString(3, user.getUserBirth());
            psmt.setString(4, user.getUserAuth().toString());
            psmt.setInt(5, user.getUserPoint());
            psmt.setString(6, user.getUserId());
            log.debug("save sql:{}", psmt);
            log.debug("connection:{}", connection);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateLatestLoginAtByUserId(String userId, LocalDateTime latestLoginAt) {
        //todo#3-6, 마지막 로그인 시간 업데이트, executeUpdate()을 반환합니다.
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "UPDATE users " +
                "SET latest_login_at=? " +
                "WHERE user_id=?";
        try {

            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setTimestamp(1, Timestamp.valueOf(latestLoginAt));
            psmt.setString(2, userId);
            log.debug("save sql:{}", psmt);
            log.debug("connection:{}", connection);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    //수정1: WHERE user_id=? 대신 user_id LIKE를 사용하여 totalcount 대신 사용할 수 있습니다.(파라미터: "%"사용)
    public int countByUserId(String userId) {
        //todo#3-7 userId와 일치하는 회원의 count를 반환합니다.

        String sql = "SELECT count(*) AS count " +
                "FROM users " +
                "WHERE user_id LIKE ?";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, userId);

            log.debug("countUser query:{}", psmt);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public Optional<User> findUserQuery(String sql, String userId, String userPassword) throws SQLException {
        Connection connection = DbConnectionThreadLocal.getConnection();

        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);
            psmt.setString(2, userPassword);
            log.debug("findUser ID/PW query:{}", psmt);

            ResultSet rs = psmt.executeQuery();

            if (rs.next()) {
                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_password"),
                        rs.getString("user_birth"),
                        User.Auth.valueOf(rs.getString("user_auth")),
                        rs.getInt("user_point"),
                        Objects.nonNull(rs.getTimestamp("created_at")) ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        Objects.nonNull(rs.getTimestamp("latest_login_at")) ? rs.getTimestamp("latest_login_at").toLocalDateTime() : null
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public Optional<User> findUserQuery(String sql, String userId) throws SQLException {
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, userId);

            ResultSet rs = psmt.executeQuery();
            log.debug("findUser ID query:{}", psmt);
            if (rs.next()) {
                User user = new User(
                        rs.getString("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_password"),
                        rs.getString("user_birth"),
                        User.Auth.valueOf(rs.getString("user_auth")),
                        rs.getInt("user_point"),
                        Objects.nonNull(rs.getTimestamp("created_at")) ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                        Objects.nonNull(rs.getTimestamp("latest_login_at")) ? rs.getTimestamp("latest_login_at").toLocalDateTime() : null
                );
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    Page<User> findAll(int page, int pageSize) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        ResultSet rs = null;

        int offset = (page - 1) * pageSize;
        int limit = pageSize;

        String sql = "SELECT user_id, user_name, user_birth, " +
                "user_auth, user_point, created_at, latest_login_at " +
                "FROM users order by user_id desc limit ?,?";

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setInt(1, offset);
            psmt.setInt(2, limit);
            rs = psmt.executeQuery();

            List<User> userList = new ArrayList<>(pageSize);

            while (rs.next()) {
                userList.add(
                        new User(
                                rs.getString("user_id"),
                                rs.getString("user_name"),
                                "**비밀**",
                                rs.getString("user_birth"),
                                User.Auth.valueOf(rs.getString("user_auth")),
                                rs.getInt("user_point"),
                                rs.getTimestamp("created_at").toLocalDateTime(),
                                rs.getTimestamp("latest_login_at").toLocalDateTime()
                        )
                );
            }

            long total = 0;

            if (!userList.isEmpty()) {
                // size>0 조회 시도, 0이면 조회할 필요 없음, count query는 자원을 많이 소모하는 작업
                total = countByUserId("%");
            }

            return new Page<User>(userList, total);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
