package com.nhnacademy.shoppingmall.user.pointdetail.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Function;

public class PointDetail {


    private final String requestId;
    private final String userId;
    private final changeInfo changeinfo;
    private final BigDecimal changeAmount;
    private final LocalDateTime changeDate;

    public enum changeInfo {
        DAILY_FIRST_LOGIN("매일 첫 로그인 적립", value -> BigDecimal.valueOf(10_000)),
        ORDER_USAGE("주문 시 사용된 포인트", value -> value.negate()),
        ORDER_REWARD("주문 보상 적립", value -> value.multiply(BigDecimal.valueOf(0.1)));
        private final String info;
        private final Function<BigDecimal, BigDecimal> expression;

        changeInfo(String info, Function<BigDecimal, BigDecimal> expression) {
            this.info = info;
            this.expression = expression;
        }

        public BigDecimal calculate(BigDecimal value) {
            return expression.apply(value);
        }

        @Override
        public String toString() {
            return info;
        }
    }

    public PointDetail(String requestId, String userId, changeInfo changeinfo, BigDecimal value, LocalDateTime changeDate) {
        this.requestId = requestId;
        this.userId = userId;
        this.changeinfo = changeinfo;
        this.changeAmount = changeinfo.calculate(value);
        this.changeDate = changeDate;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getUserId() {
        return userId;
    }

    public changeInfo getChangeinfo() {
        return changeinfo;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public LocalDateTime getChangeDate() {
        return changeDate;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PointDetail that = (PointDetail) o;
        return Objects.equals(requestId, that.requestId) && Objects.equals(userId, that.userId) && changeinfo == that.changeinfo && Objects.equals(changeAmount, that.changeAmount) && Objects.equals(changeDate, that.changeDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, userId, changeinfo, changeAmount, changeDate);
    }

    @Override
    public String toString() {
        return "PointDetail{" +
                "requestId='" + requestId + '\'' +
                ", userId='" + userId + '\'' +
                ", changeinfo=" + changeinfo +
                ", changeAmount=" + changeAmount +
                ", changeDate=" + changeDate +
                '}';
    }
}