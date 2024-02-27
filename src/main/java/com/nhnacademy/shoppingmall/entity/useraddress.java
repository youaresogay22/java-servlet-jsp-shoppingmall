package com.nhnacademy.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "useraddress")
@NoArgsConstructor
@Getter
@Setter
public class useraddress {
    @Id
    @Column(name = "useraddrID")
    private int userAddrId;

    @Column(name = "useraddress")
    private String userAddress;

    @ManyToOne
    @Column(name = "userId")
    private users user;
}
