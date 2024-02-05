package com.nhnacademy.shoppingmall.common.page;

import com.nhnacademy.shoppingmall.user.domain.User;
import lombok.Getter;

@Getter
public class PageContainer {
    private final Page<?> list;
    private final long totalPageCount;
    private final long blockStartPageNo;
    private final long blockEndPageNo;
    private final long currentPage;

    public PageContainer(Page<?> list, long totalpagecount, long blockstartpageno, long blockendpageno, long currentPage) {
        this.list = list;
        this.totalPageCount = totalpagecount;
        this.blockStartPageNo = blockstartpageno;
        this.blockEndPageNo = blockendpageno;
        this.currentPage = currentPage;
    }
}
