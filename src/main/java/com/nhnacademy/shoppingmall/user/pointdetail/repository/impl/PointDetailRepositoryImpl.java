package com.nhnacademy.shoppingmall.user.pointdetail.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;
import com.nhnacademy.shoppingmall.user.pointdetail.repository.PointDetailRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class PointDetailRepositoryImpl implements PointDetailRepository {
    @Override
    public int save(PointDetail pointDetail) {
        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            String pointSql = "INSERT INTO userpointDetail (userpointDetailID, userID, " +
                    "userpointchange, userpointchangeamount, userpointchangedate) " +
                    "VALUES (?,?,?,?,?)";

            PreparedStatement psmt = connection.prepareStatement(pointSql);
            psmt.setString(1, pointDetail.getRequestId());
            psmt.setString(2, pointDetail.getUserId());
            psmt.setString(3, pointDetail.getChangeinfo().name());
            psmt.setString(3, pointDetail.getChangeinfo().name());
            psmt.setBigDecimal(4, pointDetail.getChangeAmount());
            psmt.setTimestamp(5, Timestamp.valueOf(pointDetail.getChangeDate()));
            log.debug("point sql:{}", psmt);
            return psmt.executeUpdate();

        } catch (SQLException e) {
            log.debug("ERROR: in point sql");
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByPointDetailId(String pointDetailId) {
        String sql = "DELETE FROM userpointDetail " +
                "WHERE userpointDetailID = ?";

        return deleteDetailQuery(pointDetailId, sql);
    }

    @Override
    public int deleteByUserId(String userId) {
        String sql = "DELETE FROM userpointDetail " +
                "WHERE userID = ?";

        return deleteDetailQuery(userId, sql);
    }

    @Override
    public Optional<PointDetail> findByPointDetailId(String pointDetailId) {
        String sql = "SELECT * " +
                "FROM userpointDetail " +
                "WHERE userpointDetailID = ?";

        return findDetailQuery(pointDetailId, sql);
    }

    @Override
    public Optional<PointDetail> findByUserId(String userId) {
        String sql = "SELECT * " +
                "FROM userpointDetail " +
                "WHERE userID = ?";

        return findDetailQuery(userId, sql);
    }

    @Override
    public Page<PointDetail> pageAll(int page, int pageSize, String userId) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        ResultSet rs = null;
        int offset = (page - 1) * pageSize;
        int limit = pageSize;

        String sql = "SELECT * " +
                "FROM userpointDetail " +
                "WHERE userID = ? " +
                "ORDER BY userpointchangedate DESC LIMIT ?, ?";

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, userId);
            psmt.setInt(2, offset);
            psmt.setInt(3, limit);
            log.debug("page user query:{}", psmt);
            rs = psmt.executeQuery();
            List<PointDetail> pointList = new ArrayList<>(pageSize);

            while (rs.next()) {
                pointList.add(
                        new PointDetail(
                                rs.getString("userpointDetailID"),
                                rs.getString("userID"),
                                PointDetail.changeInfo.valueOf(rs.getString("userpointchange")),
                                rs.getBigDecimal("userpointchangeamount"),
                                Objects.nonNull(rs.getTimestamp("userpointchangedate")) ? rs.getTimestamp("userpointchangedate").toLocalDateTime() : null
                        )
                );
            }

            long total = 0;
            if (!pointList.isEmpty()) {
                // size>0 조회 시도, 0이면 조회할 필요 없음, count query는 자원을 많이 소모하는 작업
                total = countByUserId(userId);
            }

            return new Page<PointDetail>(pointList, total);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countByUserId(String userId) {
        String sql = "SELECT count(*) AS count " +
                "FROM userpointDetail " +
                "WHERE userID LIKE ?";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, userId);

            log.debug("count Point query:{}", psmt);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public int deleteDetailQuery(String userId, String sql) {
        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, userId);
            log.debug("DELETE point query:{}", psmt);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<PointDetail> findDetailQuery(String userId, String sql) {
        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, userId);

            ResultSet rs = psmt.executeQuery();
            log.debug("find point query:{}", psmt);
            if (rs.next()) {
                PointDetail pointdetail = new PointDetail(
                        rs.getString("userpointDetailID"),
                        rs.getString("userID"),
                        PointDetail.changeInfo.valueOf(rs.getString("userpointchange")),
                        rs.getBigDecimal("userpointchangeamount"),
                        Objects.nonNull(rs.getTimestamp("userpointchangedate")) ? rs.getTimestamp("userpointchangedate").toLocalDateTime() : null
                );
                return Optional.of(pointdetail);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}
