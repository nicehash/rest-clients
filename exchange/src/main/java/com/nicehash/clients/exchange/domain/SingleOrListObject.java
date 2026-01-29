package com.nicehash.clients.exchange.domain;

import java.util.List;


public class SingleOrListObject<T> extends ListObject<T> {
    private T single;

    public SingleOrListObject() {
    }

    public SingleOrListObject(List<T> list) {
        super(list);
    }

    public SingleOrListObject(T single) {
        this.single = single;
    }

    public T getSingle() {
        return single;
    }

    public void setSingle(T single) {
        this.single = single;
    }
}
