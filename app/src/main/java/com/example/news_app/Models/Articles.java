package com.example.news_app.Models;


import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Articles extends ArrayList<Parcelable> {

    private String status;
    private String userTier;
    private Integer total;
    private Integer startIndex;
    private Integer pageSize;
    private Integer currentPage;
    private Integer pages;
    private String orderBy;
    private List<Result> results = null;

    public Articles(String status, String userTier, Integer total, Integer startIndex, Integer pageSize, Integer currentPage, Integer pages, String orderBy, List<Result> results) {
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pages = pages;
        this.orderBy = orderBy;
        this.results = results;
    }

    public Articles() {
        status = "";
        userTier = "";
        total = 0;
        startIndex = 0;
        pageSize = 0;
        currentPage = 0;
        pages = 0;
        orderBy = "";
        results = new ArrayList<>();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
