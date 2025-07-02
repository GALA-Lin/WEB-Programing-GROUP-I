package com.student.webproject.news.dto;

import java.util.List;

public class PageDTO<T> {
    private long total;
    private List<T> list;

    public PageDTO(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    // Getters
    public long getTotal() { return total; }
    public List<T> getList() { return list; }
}