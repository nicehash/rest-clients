package com.nicehash.clients.exchange.domain;

import java.util.List;

public class ListObject<T> {
  private List<T> list;

  public ListObject() {}

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
