package com.gl.myparadise.common.mongo.fn;

import java.util.List;

public interface Page<T> {
    List<T> getResult();

    long getTotal();
}
