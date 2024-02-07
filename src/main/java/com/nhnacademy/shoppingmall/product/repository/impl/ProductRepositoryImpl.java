package com.nhnacademy.shoppingmall.product.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.execption.ProductNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public int save(Product product) {
        String sql = "INSERT INTO Products (ProductID, ModelNumber, ModelName, " +
                "ProductThumbnail, ProductImage, UnitCost, UnitQuantity, " +
                "Description) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, product.getProductId());
            psmt.setString(2, product.getModelNumber());
            psmt.setString(3, product.getModelName());
            psmt.setString(4, product.getProductThumbNail());
            psmt.setString(5, product.getProductImage());
            psmt.setBigDecimal(6, product.getUnitCost());
            psmt.setInt(7, product.getUnitQuantity());
            psmt.setString(8, product.getProductId());
            log.debug("save sql:{}", psmt);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int update(Product product) {
        String sql = "UPDATE Products " +
                "SET ModelNumber=?, ModelName=?, ProductThumbnail=?, " +
                "ProductImage=?, UnitCost=?, UnitQuantity=?, Description=? " +
                "WHERE ProductID=?";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, product.getModelNumber());
            psmt.setString(2, product.getModelName());
            psmt.setString(3, product.getProductThumbNail());
            psmt.setString(4, product.getProductImage());
            psmt.setBigDecimal(5, product.getUnitCost());
            psmt.setString(6, product.getDescription());
            psmt.setInt(7, product.getUnitQuantity());
            psmt.setString(8, product.getProductId());
            log.debug("update sql:{}", psmt);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int updateByProductId(String productId) {
        return 0;
    }

    @Override
    public int deleteByProductId(String productId) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        String sql = "DELETE FROM Products " +
                "WHERE ProductID = ?";

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, productId);
            log.debug("DELETE sql:{}", psmt);

            return psmt.executeUpdate();
        } catch (SQLException e) {
            throw new ProductNotFoundException(productId);
        }
    }

    @Override
    public Optional<Product> findByEverything(String field, String keyword) {
        String sql = "SELECT * " +
                "FROM Products ";
        switch (field) {
            case "ID":
                sql += "WHERE ProductID = ?";
                return findProductQuery(sql, keyword);
            case "NUMBER":
                sql += "WHERE ModelNumber = ?";
                return findProductQuery(sql, keyword);
            case "NAME":
                sql += "WHERE ModelName LIKE ?";
                return findProductQuery(sql, "%" + keyword + "%");
            case "DESCRIPTION":
                sql += "WHERE Description LIKE ?";
                return findProductQuery(sql, "%" + keyword + "%");
            default:
                return Optional.empty();
        }
    }

    @Override
    public Page<Product> pageAll(int page, int pageSize) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        ResultSet rs = null;
        int offset = (page - 1) * pageSize;
        int limit = pageSize;

        String sql = "SELECT * " +
                "FROM Products " +
                "ORDER BY UnitCost DESC LIMIT ?, ?";

        try {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setInt(1, offset);
            psmt.setInt(2, limit);
            log.debug("page product query:{}", psmt);
            rs = psmt.executeQuery();
            List<Product> productList = new ArrayList<>(pageSize);

            while (rs.next()) {
                productList.add(
                        new Product(
                                rs.getString("ProductID"),
                                rs.getString("ModelNumber"),
                                rs.getString("ModelName"),
                                rs.getString("ProductThumbnail"),
                                rs.getString("ProductImage"),
                                rs.getBigDecimal("UnitCost"),
                                rs.getInt("UnitQuantity"),
                                rs.getString("Description")
                        )
                );
            }

            long total = 0;
            if (!productList.isEmpty()) {
                // size>0 조회 시도, 0이면 조회할 필요 없음, count query는 자원을 많이 소모하는 작업
                total = countAll();
            }

            return new Page<Product>(productList, total);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int countAll() {
        String sql = "SELECT count(*) AS count " +
                "FROM Products";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);

            log.debug("count ALL Product query:{}", psmt);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    @Override
    public int countByProductId(String productId) {
        String sql = "SELECT count(*) AS count " +
                "FROM Products " +
                "WHERE ProductID LIKE ?";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, productId);

            log.debug("count Product query:{}", psmt);
            ResultSet rs = psmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    private Optional<Product> findProductQuery(String sql, String keyword) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, keyword);

            ResultSet rs = psmt.executeQuery();
            log.debug("find product query:{}", psmt);
            if (rs.next()) {
                Product product = new Product(
                        rs.getString("ProductID"),
                        rs.getString("ModelNumber"),
                        rs.getString("ModelName"),
                        rs.getString("ProductThumbnail"),
                        rs.getString("ProductImage"),
                        rs.getBigDecimal("UnitCost"),
                        rs.getInt("UnitQuantity"),
                        rs.getString("Description")
                );
                return Optional.of(product);
            }
        } catch (SQLException e) {
            throw new ProductNotFoundException(keyword);
        }
        return Optional.empty();
    }
}
