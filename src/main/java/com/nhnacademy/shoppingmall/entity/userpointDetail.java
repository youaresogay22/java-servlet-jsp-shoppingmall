package com.nhnacademy.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "userpointDetail")
@NoArgsConstructor
@Getter
@Setter
public class userpointDetail {
    @Id
    @Column(name = "userpointDetailID")
    private String userPointDetailID;

    @ManyToOne
    @JoinColumn(name = "userId")
    private users user;

    @Column(name = "userpointchange")
    private String userPointChange;

    @Column(name = "userpointchangeamount")
    private String userPointChangeAmount;

    @Column(name = "userpointchangedate")
    private String userPointChangeDate;

}
