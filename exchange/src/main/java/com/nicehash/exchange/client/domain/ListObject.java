package com.nicehash.exchange.client.domain;

import java.util.List;

/**
 * @author Ales Justin
 */
public class ListObject<T> {
    private List<T> list;

    public ListObject() {
    }

    public ListObject(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
