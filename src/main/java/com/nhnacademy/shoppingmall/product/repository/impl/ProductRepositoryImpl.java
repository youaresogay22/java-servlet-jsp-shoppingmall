package com.nhnacademy.shoppingmall.product.repository.impl;

import com.nhnacademy.shoppingmall.common.mvc.transaction.DbConnectionThreadLocal;
import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.execption.ProductNotFoundException;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.user.domain.User;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class ProductRepositoryImpl implements ProductRepository {
    @Override
    public int save(Product product) {
        String sql = "INSERT INTO Products (ProductID, ModelNumber, ModelName, " +
                "ProductThumbnail, ProductImage, UnitCost, " +
                "Description) " +
                "VALUES (?,?,?,?,?,?,?)";

        try {
            Connection connection = DbConnectionThreadLocal.getConnection();
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, product.getProductId());
            psmt.setString(2, product.getModelNumber());
            psmt.setString(3, product.getModelName());
            psmt.setBlob(4, product.getProductThumbNail());
            psmt.setBlob(5, product.getProductImage());
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
            psmt.setBlob(3, product.getProductThumbNail());
            psmt.setBlob(4, product.getProductImage());
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
                "FROM Products " +
                "WHERE ? LIKE ?";
        switch (field) {
            case "ID":
                return findProductQuery(sql, "ProductID", keyword);
            case "NUMBER":
                return findProductQuery(sql, "ModelNumber", keyword);
            case "NAME":
                return findProductQuery(sql, "ModelName", "%" + keyword + "%");
            case "DESCRIPTION":
                return findProductQuery(sql, "Description", "%" + keyword + "%");
            default:
                return Optional.empty();
        }
    }

    private Optional<Product> findProductQuery(String sql, String field, String keyword) {
        Connection connection = DbConnectionThreadLocal.getConnection();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setString(1, field);
            psmt.setString(2, keyword);

            ResultSet rs = psmt.executeQuery();
            log.debug("find product query:{}", psmt);
            if (rs.next()) {
                Product product = new Product(
                        rs.getString("ProductID"),
                        rs.getString("ModelNumber"),
                        rs.getString("ModelName"),
                        rs.getBlob("ProductThumbnail"),
                        rs.getBlob("ProductImage"),
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
