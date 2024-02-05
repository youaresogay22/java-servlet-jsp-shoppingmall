package com.nhnacademy.shoppingmall.user.pointdetail.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;
import com.nhnacademy.shoppingmall.user.pointdetail.repository.PointDetailRepository;
import com.nhnacademy.shoppingmall.user.pointdetail.service.PointDetailService;

import java.util.Optional;

public class PointDetailServiceImpl implements PointDetailService {
    private final PointDetailRepository pointrepository;

    public PointDetailServiceImpl(PointDetailRepository pointrepository) {
        this.pointrepository = pointrepository;
    }

    @Override
    public int save(PointDetail pointDetail) {
        return pointrepository.save(pointDetail);
    }

    @Override
    public int deleteLogByLogId(String pointDetailId) {
        return pointrepository.deleteByPointDetailId(pointDetailId);
    }

    @Override
    public int deleteLogByUserId(String userId) {
        return pointrepository.deleteByPointDetailId(userId);
    }

    @Override
    public Optional<PointDetail> getLogByDetailId(String pointDetailId) {
        return pointrepository.findByPointDetailId(pointDetailId);
    }

    @Override
    public Optional<PointDetail> getLogByUserId(String userId) {
        return pointrepository.findByPointDetailId(userId);
    }

    @Override
    public Page<PointDetail> getAllPageOfUser(int page, int pagesize, String userId) {
        return pointrepository.pageAll(page, pagesize, userId);
    }

    @Override
    public int countUser(String userId) {
        return pointrepository.countByUserId(userId);
    }
}
