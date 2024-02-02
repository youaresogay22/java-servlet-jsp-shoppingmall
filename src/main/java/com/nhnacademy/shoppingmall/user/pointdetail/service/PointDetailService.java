package com.nhnacademy.shoppingmall.user.pointdetail.service;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.user.pointdetail.domain.PointDetail;

import java.util.Optional;

public interface PointDetailService {
    public int save(PointDetail pointDetail);

    public int deleteLogByLogId(String pointDetailId);

    public int deleteLogByUserId(String userId);

    public Optional<PointDetail> getLogByDetailId(String pointDetailId);

    public Optional<PointDetail> getLogByUserId(String userId);

    public Page<PointDetail> getAllPageOfUser(int page, int pagesize, String userId);

    public int countUser(String userId);
}
