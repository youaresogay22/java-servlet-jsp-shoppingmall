package com.nhnacademy.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class users {
    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_birth")
    private String userBirth;

    @Column(name = "user_auth")
    private String userAuth;

    @Column(name = "user_point")
    private int userPoint;

    @Column(name = "created_at")
    private LocalDateTime atCreated;

    @Column(name = "latest_login_at")
    private LocalDateTime latestLoginAt;

}
