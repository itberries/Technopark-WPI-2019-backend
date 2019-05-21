package com.itberries.technopark.itberries.models;

import java.util.List;

public class VKUserList {
    private Integer count;
    private List<VKUser> items;
    private List<Integer> lists;

    public VKUserList() {
    }

    public List<Integer> getLists() {
        return lists;
    }

    public void setLists(List<Integer> lists) {
        this.lists = lists;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<VKUser> getItems() {
        return items;
    }

    public void setItems(List<VKUser> items) {
        this.items = items;
    }
}
