package com.nhnacademy.shoppingmall.user.pointdetail.repository;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;

import java.util.Optional;

public interface PointDetailRepository {
    public int save(PointDetail pointDetail);

    public int deleteByPointDetailId(String pointDetailId);

    public int deleteByUserId(String userId);

    public Optional<PointDetail> findByPointDetailId(String pointDetailId);

    public Optional<PointDetail> findByUserId(String userId);

    public Page<PointDetail> pageAll(int page, int pagesize, String userId);

    public int countByUserId(String userId);

}
