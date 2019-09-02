package com.shop.online.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/19 0019
 */
@Data
public class PageBean<T> implements Serializable {

    // 当前页码
    private Integer currentPage = 1;
    // 每页记录数
    private Integer pageSize = 10;
    // 总记录数
    private Integer totalCount = 0;
    // 总页数
    private Integer pageCount = 0;
    // 返回的查询结果集
    private List<T> resultList;


    public Integer getPageCount() {
        pageCount = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            pageCount++;
        }
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

}

