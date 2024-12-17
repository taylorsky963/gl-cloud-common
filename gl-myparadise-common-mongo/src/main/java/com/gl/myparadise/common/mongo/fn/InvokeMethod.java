package com.gl.myparadise.common.mongo.fn;


public interface InvokeMethod<T> {
    Page<T> invoke(Object args) throws Throwable;
}
